package com.portfolio.ae.stepdefs.api;

import com.portfolio.ae.api.clients.SearchApiClient;
import com.portfolio.ae.api.models.ApiResponse;
import com.portfolio.ae.api.models.ProductsListResponse;
import com.portfolio.ae.utils.Assertions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/** Step definitions for {@code search_api.feature}. */
public class SearchApiSteps {

    private final SearchApiClient searchApiClient = new SearchApiClient();

    private ProductsListResponse searchResponse;
    private ApiResponse contractViolationResponse;

    @When("I search products for {string}")
    public void i_search_products_for(String searchTerm) {
        searchResponse = searchApiClient.searchProduct(searchTerm);
    }

    @When("I send a search request without the {string} parameter")
    public void i_send_a_search_request_without_the_parameter(String parameterName) {
        contractViolationResponse = searchApiClient.searchProductWithoutParam();
    }

    @Then("the search API response code should be {int}")
    public void the_search_api_response_code_should_be(int expectedResponseCode) {
        int actualResponseCode = searchResponse != null
                ? searchResponse.getResponseCode()
                : contractViolationResponse.getResponseCode();
        Assertions.assertEquals(actualResponseCode, expectedResponseCode,
                "The searchProduct body responseCode does not match the expected value");
    }

    @And("the search results should not be empty")
    public void the_search_results_should_not_be_empty() {
        Assertions.assertTrue(!searchResponse.getProducts().isEmpty(),
                "The search should return at least one product");
    }
}
