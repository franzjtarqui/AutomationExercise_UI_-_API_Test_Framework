package com.portfolio.ae.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountDeletedPage extends BasePage {

    private static final By ACCOUNT_DELETED_HEADING = By.cssSelector("h2[data-qa='account-deleted']");
    private static final By CONTINUE_BUTTON = By.cssSelector("a[data-qa='continue-button']");

    public AccountDeletedPage(WebDriver driver) {
        super(driver);
    }

    public String getConfirmationMessage() {
        return textOf(ACCOUNT_DELETED_HEADING);
    }

    public HomePage clickContinue() {
        click(CONTINUE_BUTTON);
        return new HomePage(driver);
    }
}
