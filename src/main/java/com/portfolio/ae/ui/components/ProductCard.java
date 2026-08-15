package com.portfolio.ae.ui.components;

import com.portfolio.ae.ui.pages.ProductDetailPage;
import com.portfolio.ae.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Individual product card within {@code ProductsPage}'s grid (PLAN.md section 5).
 * It's identified by {@code data-product-id}, present both on {@code /products} and in
 * search results. Each product has TWO identical "Add to cart" links in the DOM
 * (one visible by default in {@code .productinfo}, another inside {@code .product-overlay} that
 * only shows on hover); this class always targets the first one to avoid depending on hover.
 */
public class ProductCard {

    private final WebDriver driver;
    private final WaitUtils waitUtils;
    private final String productId;

    public ProductCard(WebDriver driver, String productId) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return waitUtils.waitVisible(nameLocator()).getText().trim();
    }

    public String getPrice() {
        return waitUtils.waitVisible(priceLocator()).getText().trim();
    }

    public CartModal addToCart() {
        waitUtils.click(addToCartLocator());
        return new CartModal(driver);
    }

    public ProductDetailPage viewProduct() {
        waitUtils.click(viewProductLocator());
        return new ProductDetailPage(driver);
    }

    private By addToCartLocator() {
        return By.xpath("(//a[@data-product-id='" + productId + "' and contains(@class,'add-to-cart')])[1]");
    }

    private By viewProductLocator() {
        return By.cssSelector("a[href='/product_details/" + productId + "']");
    }

    private By nameLocator() {
        return By.xpath("(//a[@data-product-id='" + productId
                + "'])[1]/ancestor::div[contains(@class,'productinfo')]/p");
    }

    private By priceLocator() {
        return By.xpath("(//a[@data-product-id='" + productId
                + "'])[1]/ancestor::div[contains(@class,'productinfo')]/h2");
    }
}
