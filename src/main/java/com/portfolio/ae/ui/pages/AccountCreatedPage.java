package com.portfolio.ae.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountCreatedPage extends BasePage {

    private static final By ACCOUNT_CREATED_HEADING = By.cssSelector("h2[data-qa='account-created']");
    private static final By CONTINUE_BUTTON = By.cssSelector("a[data-qa='continue-button']");

    public AccountCreatedPage(WebDriver driver) {
        super(driver);
    }

    public String getConfirmationMessage() {
        return textOf(ACCOUNT_CREATED_HEADING);
    }

    /**
     * Leaves the "Account Created" confirmation and lands on the (logged-in) home page. The
     * Continue link only navigates to {@code /}, so after clicking it we re-navigate explicitly:
     * a click-triggered navigation right after the account-creation POST is the step that the
     * site/Cloudflare intercepts from data-center IPs (CI) with an error/empty page, while a
     * fresh {@code driver.get()} is the reliable path used by every other scenario.
     */
    public HomePage clickContinue() {
        click(CONTINUE_BUTTON);
        HomePage homePage = new HomePage(driver);
        homePage.open();
        return homePage;
    }
}
