package com.portfolio.ae.ui.components;

import com.portfolio.ae.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Top bar, present on every page: Login/Signup, Cart, "Logged in as",
 * Logout, Delete Account (PLAN.md section 5).
 */
public class HeaderComponent {

    private static final By HOME_LINK = By.cssSelector("a[href='/']");
    private static final By PRODUCTS_LINK = By.cssSelector("a[href='/products']");
    private static final By CART_LINK = By.cssSelector("a[href='/view_cart']");
    private static final By SIGNUP_LOGIN_LINK = By.cssSelector("a[href='/login']");
    private static final By LOGOUT_LINK = By.cssSelector("a[href='/logout']");
    private static final By DELETE_ACCOUNT_LINK = By.cssSelector("a[href='/delete_account']");
    private static final By LOGGED_IN_AS_LABEL = By.xpath("//a[contains(., 'Logged in as')]");

    private final WaitUtils waitUtils;

    public HeaderComponent(WebDriver driver) {
        this.waitUtils = new WaitUtils(driver);
    }

    public void clickHome() {
        waitUtils.click(HOME_LINK);
    }

    public void clickProducts() {
        waitUtils.click(PRODUCTS_LINK);
    }

    public void clickCart() {
        waitUtils.click(CART_LINK);
    }

    public void clickSignupLogin() {
        waitUtils.click(SIGNUP_LOGIN_LINK);
    }

    public void clickLogout() {
        waitUtils.click(LOGOUT_LINK);
    }

    public void clickDeleteAccount() {
        waitUtils.click(DELETE_ACCOUNT_LINK);
    }

    public boolean isSignupLoginVisible() {
        return isVisible(SIGNUP_LOGIN_LINK);
    }

    public boolean isLoggedIn() {
        return isVisible(LOGGED_IN_AS_LABEL);
    }

    public String getLoggedInUserName() {
        String fullText = waitUtils.waitVisible(LOGGED_IN_AS_LABEL).getText();
        return fullText.replace("Logged in as", "").trim();
    }

    private boolean isVisible(By locator) {
        WebElement element = waitUtils.tryWaitVisible(locator);
        return element != null && element.isDisplayed();
    }
}
