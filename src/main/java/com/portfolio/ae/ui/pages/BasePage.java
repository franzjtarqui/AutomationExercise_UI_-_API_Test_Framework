package com.portfolio.ae.ui.pages;

import com.portfolio.ae.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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
        try {
            return waitUtils.waitVisible(locator).isDisplayed();
        } catch (TimeoutException timeoutException) {
            return false;
        }
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
