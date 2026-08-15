package com.portfolio.ae.ui.pages;

import com.portfolio.ae.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * /login page: contains the login form and the "New User Signup" form side by side
 * (PLAN.md section 5). The selectors use the site's {@code data-qa} attributes, which are stable
 * against style changes.
 */
public class LoginSignupPage extends BasePage {

    private static final By LOGIN_EMAIL_INPUT = By.cssSelector("input[data-qa='login-email']");
    private static final By LOGIN_PASSWORD_INPUT = By.cssSelector("input[data-qa='login-password']");
    private static final By LOGIN_BUTTON = By.cssSelector("button[data-qa='login-button']");
    private static final By LOGIN_ERROR_MESSAGE = By.cssSelector(".login-form p");

    private static final By SIGNUP_NAME_INPUT = By.cssSelector("input[data-qa='signup-name']");
    private static final By SIGNUP_EMAIL_INPUT = By.cssSelector("input[data-qa='signup-email']");
    private static final By SIGNUP_BUTTON = By.cssSelector("button[data-qa='signup-button']");
    private static final By SIGNUP_ERROR_MESSAGE = By.cssSelector(".signup-form p");

    public LoginSignupPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        open(ConfigManager.getBaseUrlUi() + "/login");
    }

    public void login(String email, String password) {
        type(LOGIN_EMAIL_INPUT, email);
        type(LOGIN_PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
    }

    public String getLoginErrorMessage() {
        return textOf(LOGIN_ERROR_MESSAGE);
    }

    /**
     * Completes the first signup step ("Name" + "Email Address") and confirms. The site redirects
     * to the full details page ({@link SignupDetailsPage}) only if the email doesn't already
     * exist; if it does, it stays on this same page showing {@link #getSignupErrorMessage()}.
     */
    public void signup(String name, String email) {
        type(SIGNUP_NAME_INPUT, name);
        type(SIGNUP_EMAIL_INPUT, email);
        click(SIGNUP_BUTTON);
    }

    public String getSignupErrorMessage() {
        return textOf(SIGNUP_ERROR_MESSAGE);
    }
}
