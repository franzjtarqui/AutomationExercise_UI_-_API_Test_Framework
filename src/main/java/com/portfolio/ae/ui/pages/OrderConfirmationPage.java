package com.portfolio.ae.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Final confirmation ({@code /payment_done/{orderId}}): "Order Placed!" (PLAN.md section 5).
 */
public class OrderConfirmationPage extends BasePage {

    private static final By ORDER_PLACED_HEADING = By.cssSelector("h2[data-qa='order-placed']");
    private static final By CONTINUE_BUTTON = By.cssSelector("a[data-qa='continue-button']");

    public OrderConfirmationPage(WebDriver driver) {
        super(driver);
    }

    public String getConfirmationMessage() {
        return textOf(ORDER_PLACED_HEADING);
    }

    public HomePage clickContinue() {
        click(CONTINUE_BUTTON);
        return new HomePage(driver);
    }
}
