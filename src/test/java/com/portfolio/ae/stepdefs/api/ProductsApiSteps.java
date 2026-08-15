package com.portfolio.ae.stepdefs.api;

import com.portfolio.ae.api.clients.ProductApiClient;
import com.portfolio.ae.api.models.ApiResponse;
import com.portfolio.ae.api.models.ProductsListResponse;
import com.portfolio.ae.utils.Assertions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/** Step definitions for {@code products_api.feature}. */
public class ProductsApiSteps {

    private final ProductApiClient productApiClient = new ProductApiClient();

    private ProductsListResponse productsListResponse;
    private ApiResponse contractViolationResponse;

    @When("I request the full products list")
    public void i_request_the_full_products_list() {
        productsListResponse = productApiClient.getProductsList();
    }

    @When("I send a POST request to the products list endpoint")
    public void i_send_a_post_request_to_the_products_list_endpoint() {
        contractViolationResponse = productApiClient.postProductsList();
    }

    @Then("the products API response code should be {int}")
    public void the_products_api_response_code_should_be(int expectedResponseCode) {
        int actualResponseCode = productsListResponse != null
                ? productsListResponse.getResponseCode()
                : contractViolationResponse.getResponseCode();
        Assertions.assertEquals(actualResponseCode, expectedResponseCode,
                "The productsList body responseCode does not match the expected value");
    }

    @And("the products list should not be empty")
    public void the_products_list_should_not_be_empty() {
        Assertions.assertTrue(!productsListResponse.getProducts().isEmpty(),
                "The products list should not be empty");
    }
}
