package com.portfolio.ae.stepdefs.api;

import com.portfolio.ae.api.clients.BrandApiClient;
import com.portfolio.ae.api.models.ApiResponse;
import com.portfolio.ae.api.models.BrandsListResponse;
import com.portfolio.ae.utils.Assertions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/** Step definitions for {@code brands_api.feature}. */
public class BrandsApiSteps {

    private final BrandApiClient brandApiClient = new BrandApiClient();

    private BrandsListResponse brandsListResponse;
    private ApiResponse contractViolationResponse;

    @When("I request the full brands list")
    public void i_request_the_full_brands_list() {
        brandsListResponse = brandApiClient.getBrandsList();
    }

    @When("I send a PUT request to the brands list endpoint")
    public void i_send_a_put_request_to_the_brands_list_endpoint() {
        contractViolationResponse = brandApiClient.putBrandsList();
    }

    @Then("the brands API response code should be {int}")
    public void the_brands_api_response_code_should_be(int expectedResponseCode) {
        int actualResponseCode = brandsListResponse != null
                ? brandsListResponse.getResponseCode()
                : contractViolationResponse.getResponseCode();
        Assertions.assertEquals(actualResponseCode, expectedResponseCode,
                "The brandsList body responseCode does not match the expected value");
    }

    @And("the brands list should not be empty")
    public void the_brands_list_should_not_be_empty() {
        Assertions.assertTrue(!brandsListResponse.getBrands().isEmpty(),
                "The brands list should not be empty");
    }
}
