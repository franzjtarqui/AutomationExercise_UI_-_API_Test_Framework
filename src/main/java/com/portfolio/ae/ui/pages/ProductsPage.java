package com.portfolio.ae.ui.pages;

import com.portfolio.ae.config.ConfigManager;
import com.portfolio.ae.ui.components.ProductCard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Products grid ({@code /products}), with search by name (PLAN.md section 5). The same
 * page is reused for search results via {@code ?search=}; the heading changes between
 * "All Products" and "Searched Products".
 */
public class ProductsPage extends BasePage {

    private static final By SEARCH_INPUT = By.id("search_product");
    private static final By SEARCH_BUTTON = By.id("submit_search");
    private static final By LISTING_HEADING = By.cssSelector(".features_items h2.title");
    private static final By PRODUCT_NAME_CELLS = By.cssSelector(".product-image-wrapper .productinfo p");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        open(ConfigManager.getBaseUrlUi() + "/products");
    }

    public void searchProduct(String query) {
        type(SEARCH_INPUT, query);
        click(SEARCH_BUTTON);
    }

    public String getListingHeading() {
        return textOf(LISTING_HEADING);
    }

    public ProductCard product(String productId) {
        return new ProductCard(driver, productId);
    }

    public List<String> getVisibleProductNames() {
        waitUtils.waitVisible(PRODUCT_NAME_CELLS);
        List<WebElement> nameCells = driver.findElements(PRODUCT_NAME_CELLS);
        return nameCells.stream().map(WebElement::getText).map(String::trim).collect(Collectors.toList());
    }

    public int getVisibleProductCount() {
        return driver.findElements(PRODUCT_NAME_CELLS).size();
    }
}
