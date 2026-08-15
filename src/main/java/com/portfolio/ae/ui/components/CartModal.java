package com.portfolio.ae.ui.components;

import com.portfolio.ae.ui.pages.CartPage;
import com.portfolio.ae.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * "Added! Your product has been added to cart." modal that appears after an "Add to cart",
 * both from the products grid and from {@code ProductDetailPage} (PLAN.md section 5).
 */
public class CartModal {

    private static final By MODAL = By.id("cartModal");
    private static final By TITLE = By.cssSelector("#cartModal .modal-title");
    private static final By VIEW_CART_LINK = By.cssSelector("#cartModal a[href='/view_cart']");
    private static final By CONTINUE_SHOPPING_BUTTON = By.cssSelector("#cartModal .close-modal");

    private final WebDriver driver;
    private final WaitUtils waitUtils;

    public CartModal(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    public String getTitle() {
        return waitUtils.waitVisible(TITLE).getText().trim();
    }

    public CartPage clickViewCart() {
        waitUtils.click(VIEW_CART_LINK);
        return new CartPage(driver);
    }

    public void clickContinueShopping() {
        waitUtils.click(CONTINUE_SHOPPING_BUTTON);
        waitUtils.waitInvisible(MODAL);
    }
}
