package com.portfolio.ae.ui.pages;

import com.portfolio.ae.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Base for all Page Objects: exposes only actions and state queries (PLAN.md section 5).
 * Assertions do NOT live here, they live in the step definitions (using {@code utils.Assertions}).
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils waitUtils;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    protected void open(String url) {
        driver.get(url);
        // The site sits behind Cloudflare, which sometimes serves a "Just a moment..." challenge
        // to headless Selenium traffic (notably from data-center IPs like GitHub Actions) instead
        // of the real page; wait it out so the first element wait on the new page doesn't time out.
        waitUtils.resolveCloudflareChallengeIfPresent();
    }

    protected void click(By locator) {
        waitUtils.click(locator);
    }

    protected void type(By locator, String text) {
        WebElement element = waitUtils.waitVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String textOf(By locator) {
        return waitUtils.waitVisible(locator).getText().trim();
    }

    protected boolean isVisible(By locator) {
        WebElement element = waitUtils.tryWaitVisible(locator);
        return element != null && element.isDisplayed();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
