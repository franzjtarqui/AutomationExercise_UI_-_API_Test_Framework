package com.portfolio.ae.stepdefs.ui;

import com.portfolio.ae.driver.DriverManager;
import com.portfolio.ae.ui.components.CartModal;
import com.portfolio.ae.ui.pages.CartPage;
import com.portfolio.ae.ui.pages.ProductDetailPage;
import com.portfolio.ae.ui.pages.ProductsPage;
import com.portfolio.ae.utils.Assertions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

/**
 * Step definitions for {@code cart.feature}. Reuses "Given I am on the products page" and
 * "When I view the details of product ..." declared in {@link ProductsSearchSteps} (same
 * glue path); no step depends on fields from that class, each one reconstructs its Page Object
 * from the current {@link WebDriver}.
 */
public class CartSteps {

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    @When("I add product {string} to the cart")
    public void i_add_product_to_the_cart(String productId) {
        new ProductsPage(driver()).product(productId).addToCart();
    }

    @And("I view the cart from the confirmation modal")
    public void i_view_the_cart_from_the_confirmation_modal() {
        new CartModal(driver()).clickViewCart();
    }

    @And("I continue shopping")
    public void i_continue_shopping() {
        new CartModal(driver()).clickContinueShopping();
    }

    @And("I set the quantity to {string}")
    public void i_set_the_quantity_to(String quantity) {
        new ProductDetailPage(driver()).setQuantity(Integer.parseInt(quantity));
    }

    @And("I add the product to the cart from the detail page")
    public void i_add_the_product_to_the_cart_from_the_detail_page() {
        new ProductDetailPage(driver()).addToCart();
    }

    @And("I remove product {string} from the cart")
    public void i_remove_product_from_the_cart(String productId) {
        new CartPage(driver()).removeProduct(productId);
    }

    @Then("the cart should contain product {string} with quantity {string}")
    public void the_cart_should_contain_product_with_quantity(String productId, String expectedQuantity) {
        CartPage cartPage = new CartPage(driver());
        Assertions.assertTrue(cartPage.containsProduct(productId),
                "The cart should contain product " + productId);
        Assertions.assertEquals(cartPage.getQuantity(productId), expectedQuantity,
                "The quantity of product " + productId + " in the cart should match");
    }

    @Then("the cart should not contain product {string}")
    public void the_cart_should_not_contain_product(String productId) {
        Assertions.assertFalse(new CartPage(driver()).containsProduct(productId),
                "The cart should not contain product " + productId);
    }
}
