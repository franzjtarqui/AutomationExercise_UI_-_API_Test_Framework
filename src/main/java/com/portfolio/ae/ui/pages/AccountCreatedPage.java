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

    public HomePage clickContinue() {
        click(CONTINUE_BUTTON);
        return new HomePage(driver);
    }
}
