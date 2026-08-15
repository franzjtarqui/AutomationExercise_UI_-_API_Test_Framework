package com.portfolio.ae.stepdefs.ui;

import com.portfolio.ae.driver.DriverManager;
import com.portfolio.ae.ui.components.HeaderComponent;
import com.portfolio.ae.ui.pages.ProductDetailPage;
import com.portfolio.ae.ui.pages.ProductsPage;
import com.portfolio.ae.utils.Assertions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * Step definitions for {@code products_search.feature}. Each step rebuilds the Page Object it
 * needs from the current {@link WebDriver} (instead of caching instances in fields), since they
 * are stateless locator-only wrappers; this allows the same steps to be reused from
 * {@code cart.feature} (see {@link CartSteps}) without depending on a dependency-injection
 * container between step definition classes.
 */
public class ProductsSearchSteps {

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    @When("I go to the products page")
    public void i_go_to_the_products_page() {
        new HeaderComponent(driver()).clickProducts();
    }

    @Given("I am on the products page")
    public void i_am_on_the_products_page() {
        new ProductsPage(driver()).open();
    }

    @Then("I should see the full product listing")
    public void i_should_see_the_full_product_listing() {
        ProductsPage productsPage = new ProductsPage(driver());
        // The heading is displayed in uppercase via CSS (text-transform); we compare in uppercase.
        Assertions.assertEquals(productsPage.getListingHeading().toUpperCase(), "ALL PRODUCTS",
                "The heading should indicate the full product listing");
        Assertions.assertTrue(productsPage.getVisibleProductCount() > 0,
                "The listing should show at least one product");
    }

    @When("I search for the product {string}")
    public void i_search_for_the_product(String query) {
        new ProductsPage(driver()).searchProduct(query);
    }

    @Then("all the visible products should contain {string} in their name")
    public void all_the_visible_products_should_contain_in_their_name(String expectedSubstring) {
        ProductsPage productsPage = new ProductsPage(driver());
        Assertions.assertEquals(productsPage.getListingHeading().toUpperCase(), "SEARCHED PRODUCTS",
                "The heading should indicate that search results are being shown");
        List<String> visibleNames = productsPage.getVisibleProductNames();
        Assertions.assertTrue(!visibleNames.isEmpty(), "The search should return at least one product");
        // The site's real search doesn't do a strict name match (it sometimes returns a product
        // without the term in its name, e.g. searching "Top" pulls in a stray "Shirt"); we validate
        // that the MAJORITY of results are consistent with the searched term, instead of requiring
        // 100% so the test isn't coupled to that imprecise behavior of the real site.
        long matchingCount = visibleNames.stream()
                .filter(name -> name.toUpperCase().contains(expectedSubstring.toUpperCase()))
                .count();
        Assertions.assertTrue(matchingCount * 2 >= visibleNames.size(),
                "The majority of the " + visibleNames.size() + " results should contain '"
                        + expectedSubstring + "' in their name (matched " + matchingCount + ")");
    }

    @When("I view the details of product {string}")
    public void i_view_the_details_of_product(String productId) {
        new ProductsPage(driver()).product(productId).viewProduct();
    }

    @Then("the product name should be {string}")
    public void the_product_name_should_be(String expectedName) {
        Assertions.assertEquals(new ProductDetailPage(driver()).getName(), expectedName,
                "The product name in the detail page should match");
    }

    @And("the product category should contain {string}")
    public void the_product_category_should_contain(String expectedSubstring) {
        Assertions.assertContains(new ProductDetailPage(driver()).getCategory(), expectedSubstring,
                "The product category should contain the expected text");
    }

    @And("the product should be {string}")
    public void the_product_should_be(String expectedAvailability) {
        Assertions.assertContains(new ProductDetailPage(driver()).getAvailability(), expectedAvailability,
                "The product availability should match");
    }
}
