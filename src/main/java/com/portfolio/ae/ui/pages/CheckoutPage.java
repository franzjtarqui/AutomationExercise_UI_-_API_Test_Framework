package com.portfolio.ae.ui.pages;

import com.portfolio.ae.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Address and order review before paying ({@code /checkout}), PLAN.md section 5. Only
 * accessible with a logged-in session: if accessed without login, the site shows a modal
 * ("Register / Login account to proceed on checkout.") instead of this page; that case is
 * out of scope for {@code checkout.feature} (PLAN.md section 3, which requires a logged-in user).
 */
public class CheckoutPage extends BasePage {

    private static final By DELIVERY_ADDRESS = By.id("address_delivery");
    private static final By BILLING_ADDRESS = By.id("address_invoice");
    private static final By ORDER_COMMENT_TEXTAREA = By.cssSelector("textarea[name='message']");
    private static final By TOTAL_AMOUNT = By.cssSelector("#cart_info table tbody tr:last-child .cart_total_price");
    private static final By PLACE_ORDER_BUTTON = By.cssSelector("a.check_out[href='/payment']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        open(ConfigManager.getBaseUrlUi() + "/checkout");
    }

    public String getDeliveryAddress() {
        return textOf(DELIVERY_ADDRESS);
    }

    public String getBillingAddress() {
        return textOf(BILLING_ADDRESS);
    }

    /** "Total Amount" row at the bottom of the order review (sum of all items). */
    public String getTotalAmount() {
        return textOf(TOTAL_AMOUNT);
    }

    public void enterOrderComment(String comment) {
        type(ORDER_COMMENT_TEXTAREA, comment);
    }

    public PaymentPage placeOrder() {
        click(PLACE_ORDER_BUTTON);
        return new PaymentPage(driver);
    }
}
