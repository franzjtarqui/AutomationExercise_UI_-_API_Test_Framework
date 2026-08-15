package com.portfolio.ae.ui.pages;

import com.portfolio.ae.config.ConfigManager;
import com.portfolio.ae.ui.components.FooterComponent;
import com.portfolio.ae.ui.components.HeaderComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Landing page: access to sidebar categories/brands and entry to products via the header
 * (PLAN.md section 5). Composes {@link HeaderComponent} and {@link FooterComponent}, which are
 * present on (almost) every page of the site.
 */
public class HomePage extends BasePage {

    private final HeaderComponent header;
    private final FooterComponent footer;

    public HomePage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
        this.footer = new FooterComponent(driver);
    }

    public HeaderComponent header() {
        return header;
    }

    public FooterComponent footer() {
        return footer;
    }

    public void open() {
        open(ConfigManager.getBaseUrlUi());
    }

    public boolean isCategoryVisible(String categoryName) {
        return isVisible(categoryHeadingLocator(categoryName));
    }

    public void expandCategory(String categoryName) {
        click(categoryHeadingLocator(categoryName));
    }

    /**
     * Expands the category and clicks the subcategory within the expanded panel.
     * E.g.: selectSubCategory("Women", "Dress"). The accordion always starts collapsed on a
     * freshly loaded home page, so always expanding (without checking visibility first) avoids
     * waiting out the full explicit wait timeout against a still-hidden element.
     */
    public void selectSubCategory(String categoryName, String subCategoryName) {
        expandCategory(categoryName);
        click(subCategoryLinkLocator(categoryName, subCategoryName));
    }

    public void selectBrand(String brandName) {
        click(brandLinkLocator(brandName));
    }

    private By categoryHeadingLocator(String categoryName) {
        return By.xpath("//div[@class='panel-heading']//a[contains(., '" + categoryName + "')]");
    }

    private By subCategoryLinkLocator(String categoryName, String subCategoryName) {
        return By.xpath("//div[@id='" + categoryName + "']//a[contains(., '" + subCategoryName + "')]");
    }

    private By brandLinkLocator(String brandName) {
        return By.xpath("//div[@class='brands_products']//a[contains(., '" + brandName + "')]");
    }
}
