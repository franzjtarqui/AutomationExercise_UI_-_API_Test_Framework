package com.portfolio.ae.utils;

import com.portfolio.ae.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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
        return newWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitClickable(By locator) {
        return newWebDriverWait().until(ExpectedConditions.elementToBeClickable(locator));
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
