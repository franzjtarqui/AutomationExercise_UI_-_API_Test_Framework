package com.portfolio.ae.ui.components;

import com.portfolio.ae.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page footer: email subscription (PLAN.md section 5). The id "susbscribe_email" reflects
 * a real typo in the site's markup (it's not a mistake on our part).
 */
public class FooterComponent {

    private static final By SUBSCRIBE_EMAIL_INPUT = By.id("susbscribe_email");
    private static final By SUBSCRIBE_BUTTON = By.id("subscribe");
    private static final By SUBSCRIBE_SUCCESS_MESSAGE = By.cssSelector("#success-subscribe .alert-success");

    private final WebDriver driver;
    private final WaitUtils waitUtils;

    public FooterComponent(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    public void subscribe(String email) {
        WebElement input = waitUtils.waitVisible(SUBSCRIBE_EMAIL_INPUT);
        waitUtils.scrollIntoView(input);
        input.clear();
        input.sendKeys(email);
        waitUtils.click(SUBSCRIBE_BUTTON);
    }

    public String getSubscriptionSuccessMessage() {
        return waitUtils.waitVisible(SUBSCRIBE_SUCCESS_MESSAGE).getText().trim();
    }
}
