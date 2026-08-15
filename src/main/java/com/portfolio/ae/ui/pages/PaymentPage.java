package com.portfolio.ae.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Dummy card data and payment confirmation ({@code /payment}), PLAN.md section 5. The site
 * doesn't process real payments: it accepts any card number with the correct format.
 */
public class PaymentPage extends BasePage {

    private static final By NAME_ON_CARD_INPUT = By.cssSelector("input[data-qa='name-on-card']");
    private static final By CARD_NUMBER_INPUT = By.cssSelector("input[data-qa='card-number']");
    private static final By CVC_INPUT = By.cssSelector("input[data-qa='cvc']");
    private static final By EXPIRY_MONTH_INPUT = By.cssSelector("input[data-qa='expiry-month']");
    private static final By EXPIRY_YEAR_INPUT = By.cssSelector("input[data-qa='expiry-year']");
    private static final By PAY_BUTTON = By.cssSelector("button[data-qa='pay-button']");

    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    public void enterCardDetails(String nameOnCard, String cardNumber, String cvc, String expiryMonth, String expiryYear) {
        type(NAME_ON_CARD_INPUT, nameOnCard);
        type(CARD_NUMBER_INPUT, cardNumber);
        type(CVC_INPUT, cvc);
        type(EXPIRY_MONTH_INPUT, expiryMonth);
        type(EXPIRY_YEAR_INPUT, expiryYear);
    }

    public OrderConfirmationPage payAndConfirm() {
        click(PAY_BUTTON);
        return new OrderConfirmationPage(driver);
    }
}
