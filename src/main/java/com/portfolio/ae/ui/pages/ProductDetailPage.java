package com.portfolio.ae.ui.pages;

import com.portfolio.ae.config.ConfigManager;
import com.portfolio.ae.ui.components.CartModal;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Product detail page ({@code /product_details/{id}}): name, price, category,
 * availability, quantity, and adding to cart (PLAN.md section 5).
 */
public class ProductDetailPage extends BasePage {

    private static final By PRODUCT_NAME = By.cssSelector(".product-information h2");
    private static final By CATEGORY = By.xpath("//div[contains(@class,'product-information')]/p[contains(.,'Category')]");
    private static final By PRICE = By.cssSelector(".product-information > span > span");
    private static final By AVAILABILITY = By.xpath("//div[contains(@class,'product-information')]/p[contains(.,'Availability')]");
    private static final By CONDITION = By.xpath("//div[contains(@class,'product-information')]/p[contains(.,'Condition')]");
    private static final By BRAND = By.xpath("//div[contains(@class,'product-information')]/p[contains(.,'Brand')]");
    private static final By QUANTITY_INPUT = By.id("quantity");
    private static final By ADD_TO_CART_BUTTON = By.cssSelector(".product-information button.cart");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Named differently from {@link BasePage#open(String)} on purpose: a method
     * {@code open(String)} in this subclass would have the SAME signature as {@code BasePage}'s
     * and would override it, so calling {@code open(url)} from inside would end up invoking
     * itself (infinite recursion / StackOverflowError) instead of going up to the base class.
     */
    public void openProduct(String productId) {
        open(ConfigManager.getBaseUrlUi() + "/product_details/" + productId);
    }

    public String getName() {
        return textOf(PRODUCT_NAME);
    }

    public String getCategory() {
        return textOf(CATEGORY);
    }

    public String getPrice() {
        return textOf(PRICE);
    }

    public String getAvailability() {
        return textOf(AVAILABILITY);
    }

    public String getCondition() {
        return textOf(CONDITION);
    }

    public String getBrand() {
        return textOf(BRAND);
    }

    public void setQuantity(int quantity) {
        type(QUANTITY_INPUT, String.valueOf(quantity));
    }

    public CartModal addToCart() {
        click(ADD_TO_CART_BUTTON);
        return new CartModal(driver);
    }
}
