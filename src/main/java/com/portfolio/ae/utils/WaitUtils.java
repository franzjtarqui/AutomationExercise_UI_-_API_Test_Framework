package com.portfolio.ae.utils;

import com.portfolio.ae.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Locale;

/**
 * Encapsulates the framework's waiting strategy (PLAN.md section 8):
 * <ul>
 *   <li>Explicit waits ({@link WebDriverWait} + {@link ExpectedConditions}) as the standard.</li>
 *   <li>{@link FluentWait} reserved for custom polling cases ignoring exceptions
 *       (elements that re-render, modals, intermittent overlays/ads).</li>
 *   <li>No implicit waits, no {@code Thread.sleep}.</li>
 * </ul>
 * Timeouts come from {@link ConfigManager} (configurable via config.properties / CI).
 */
public class WaitUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitUtils.class);

    /**
     * Markers of the Cloudflare "Just a moment..." interstitial. When it's up, the real page
     * content is absent, so every wait/click on a page element times out with a misleading
     * {@link NoSuchElementException} (PLAN.md section 10; this is why the CI runs that hit it
     * failed in the 6 scenarios that navigate right after account creation).
     */
    private static final String CLOUDFLARE_CHALLENGE_TITLE = "just a moment";
    private static final By CLOUDFLARE_CHALLENGE_ELEMENT = By.cssSelector("#challenge-form, #challenge-running");

    private final WebDriver driver;
    private final Duration explicitTimeout;
    private final Duration pollingInterval;

    public WaitUtils(WebDriver driver) {
        this(driver,
                Duration.ofSeconds(ConfigManager.getExplicitWaitSeconds()),
                Duration.ofMillis(ConfigManager.getPollingMillis()));
    }

    public WaitUtils(WebDriver driver, Duration explicitTimeout, Duration pollingInterval) {
        this.driver = driver;
        this.explicitTimeout = explicitTimeout;
        this.pollingInterval = pollingInterval;
    }

    public WebElement waitVisible(By locator) {
        return waitForElement(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitClickable(By locator) {
        return waitForElement(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * True while the page is a Cloudflare challenge (title "Just a moment..." or a challenge
     * element in the DOM). Defensive: never throws, so it's safe to call on every wait.
     */
    public boolean isCloudflareChallengePresent() {
        try {
            String title = driver.getTitle();
            if (title != null && title.toLowerCase(Locale.ROOT).contains(CLOUDFLARE_CHALLENGE_TITLE)) {
                return true;
            }
            return !driver.findElements(CLOUDFLARE_CHALLENGE_ELEMENT).isEmpty();
        } catch (WebDriverException duringPageLoad) {
            return false;
        }
    }

    /**
     * If a Cloudflare challenge is up, waits (bounded by the explicit timeout) for its JS to
     * resolve, which in a real browser redirects to the actual page after a few seconds. No-op
     * when there is no challenge. Called after navigations and transparently inside waits.
     */
    public void resolveCloudflareChallengeIfPresent() {
        if (!isCloudflareChallengePresent()) {
            return;
        }
        LOGGER.warn("Cloudflare challenge detected (title='{}'), waiting up to {}s for it to resolve",
                driver.getTitle(), explicitTimeout.toSeconds());
        try {
            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(explicitTimeout)
                    .pollingEvery(Duration.ofMillis(500))
                    .ignoring(WebDriverException.class);
            wait.until(d -> !isCloudflareChallengePresent());
            LOGGER.info("Cloudflare challenge resolved (title='{}')", driver.getTitle());
        } catch (TimeoutException unresolved) {
            LOGGER.warn("Cloudflare challenge did not resolve within {}s", explicitTimeout.toSeconds());
        }
    }

    /**
     * Standard explicit wait, with one Cloudflare mitigation: if it times out while a challenge
     * is present, it waits for the challenge to clear and retries the condition once. When there
     * is no challenge (the normal case, including genuine failures) the original timeout is
     * rethrown without extra delay.
     */
    private WebElement waitForElement(ExpectedCondition<WebElement> condition) {
        try {
            return newWebDriverWait().until(condition);
        } catch (TimeoutException firstTimeout) {
            if (isCloudflareChallengePresent()) {
                LOGGER.info("Element wait timed out while a Cloudflare challenge is present; resolving and retrying once");
                resolveCloudflareChallengeIfPresent();
                if (!isCloudflareChallengePresent()) {
                    return newWebDriverWait().until(condition);
                }
            }
            throw firstTimeout;
        }
    }

    public boolean waitInvisible(By locator) {
        return newWebDriverWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitStaleness(WebElement element) {
        return newWebDriverWait().until(ExpectedConditions.stalenessOf(element));
    }

    public void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
    }

    /**
     * Safe click: waits for clickability, scrolls into view, and retries once
     * if the element becomes "stale" (a re-render typical of the site's overlays/ads). If an
     * overlay (banner/ads iframe) intercepts the mouse click, it falls back to a JS click,
     * which doesn't depend on the on-screen position.
     */
    public void click(By locator) {
        try {
            clickOnce(locator);
        } catch (StaleElementReferenceException staleElementReferenceException) {
            clickOnce(locator);
        } catch (ElementClickInterceptedException elementClickInterceptedException) {
            clickViaJavascript(locator);
        }
    }

    private void clickOnce(By locator) {
        WebElement element = waitClickable(locator);
        scrollIntoView(element);
        element.click();
    }

    private void clickViaJavascript(By locator) {
        WebElement element = waitClickable(locator);
        scrollIntoView(element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Fluent wait with custom polling, ignoring {@link NoSuchElementException} and
     * {@link StaleElementReferenceException}. Reserved for specific cases of intermittent
     * elements (modals, ad iframes) where the standard explicit wait isn't enough.
     */
    public WebElement fluentWaitVisible(By locator, Duration timeout, Duration polling) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(polling)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        return wait.until(d -> {
            WebElement element = d.findElement(locator);
            return element.isDisplayed() ? element : null;
        });
    }

    private Wait<WebDriver> newWebDriverWait() {
        return new WebDriverWait(driver, explicitTimeout, pollingInterval);
    }
}
