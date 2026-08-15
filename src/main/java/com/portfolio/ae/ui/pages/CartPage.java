package com.portfolio.ae.ui.pages;

import com.portfolio.ae.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Shopping cart ({@code /view_cart}): rows per product, subtotal per row, remove,
 * and "Proceed To Checkout" (PLAN.md section 5). The quantity of each row is NOT inline-editable
 * on this site (it's a {@code <button disabled>}); to change it you have to go back to
 * {@link ProductDetailPage#setQuantity(int)} before adding to the cart.
 */
public class CartPage extends BasePage {

    private static final By EMPTY_CART_MESSAGE = By.id("empty_cart");
    private static final By PROCEED_TO_CHECKOUT_BUTTON = By.cssSelector("a.check_out");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        open(ConfigManager.getBaseUrlUi() + "/view_cart");
    }

    public boolean isEmpty() {
        return isVisible(EMPTY_CART_MESSAGE);
    }

    public boolean containsProduct(String productId) {
        return !driver.findElements(rowLocator(productId)).isEmpty();
    }

    public String getProductName(String productId) {
        return textOf(By.cssSelector(rowSelector(productId) + " .cart_description h4 a"));
    }

    public String getUnitPrice(String productId) {
        return textOf(By.cssSelector(rowSelector(productId) + " .cart_price p"));
    }

    public String getQuantity(String productId) {
        return textOf(By.cssSelector(rowSelector(productId) + " .cart_quantity button"));
    }

    public String getRowTotal(String productId) {
        return textOf(By.cssSelector(rowSelector(productId) + " .cart_total_price"));
    }

    public void removeProduct(String productId) {
        click(By.cssSelector(rowSelector(productId) + " .cart_quantity_delete"));
        waitUtils.waitInvisible(rowLocator(productId));
    }

    public void clickProceedToCheckout() {
        click(PROCEED_TO_CHECKOUT_BUTTON);
    }

    private String rowSelector(String productId) {
        return "#product-" + productId;
    }

    private By rowLocator(String productId) {
        return By.cssSelector(rowSelector(productId));
    }
}
