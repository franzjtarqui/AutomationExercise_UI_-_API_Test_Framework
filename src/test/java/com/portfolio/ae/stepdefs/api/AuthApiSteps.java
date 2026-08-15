package com.portfolio.ae.stepdefs.api;

import com.portfolio.ae.api.clients.AccountApiClient;
import com.portfolio.ae.api.clients.AuthApiClient;
import com.portfolio.ae.api.models.AccountRequest;
import com.portfolio.ae.api.models.ApiResponse;
import com.portfolio.ae.utils.Assertions;
import com.portfolio.ae.utils.FakerUtil;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Step definitions for {@code auth_api.feature}. */
public class AuthApiSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthApiSteps.class);
    private static final String SEED_PASSWORD = "Passw0rd!123";

    private final AccountApiClient accountApiClient = new AccountApiClient();
    private final AuthApiClient authApiClient = new AuthApiClient();

    private String seedEmail;
    private boolean seedAccountPendingCleanup;
    private ApiResponse verifyLoginResponse;

    @Given("a registered user created via the API")
    public void a_registered_user_created_via_the_api() {
        seedEmail = FakerUtil.uniqueEmail();
        AccountRequest seedAccount = AccountRequest.anAccount()
                .withName("Auth Api Seed")
                .withEmail(seedEmail)
                .withPassword(SEED_PASSWORD)
                .withFirstName("Auth")
                .withLastName("Seed")
                .withAddress1("123 Seed St")
                .withCity("Seed City")
                .withState("CA")
                .withZipcode("90001")
                .withMobileNumber("5551234567");

        ApiResponse response = accountApiClient.createAccount(seedAccount);
        Assertions.assertEquals(response.getResponseCode(), 201,
                "The seed user should be created successfully before verifying login");
        seedAccountPendingCleanup = true;
    }

    @When("I verify login with that user's credentials")
    public void i_verify_login_with_that_user_s_credentials() {
        verifyLoginResponse = authApiClient.verifyLogin(seedEmail, SEED_PASSWORD);
    }

    @When("I verify login with an email that does not exist")
    public void i_verify_login_with_an_email_that_does_not_exist() {
        verifyLoginResponse = authApiClient.verifyLogin(FakerUtil.uniqueEmail(), "wrong-password");
    }

    @When("I send a verify login request without the {string} parameter")
    public void i_send_a_verify_login_request_without_the_parameter(String parameterName) {
        verifyLoginResponse = authApiClient.verifyLoginWithoutEmail("some-password");
    }

    @When("I send a DELETE request to the verify login endpoint")
    public void i_send_a_delete_request_to_the_verify_login_endpoint() {
        verifyLoginResponse = authApiClient.verifyLoginViaUnsupportedMethod();
    }

    @Then("the verify login response code should be {int}")
    public void the_verify_login_response_code_should_be(int expectedResponseCode) {
        Assertions.assertEquals(verifyLoginResponse.getResponseCode(), expectedResponseCode,
                "The verifyLogin body responseCode does not match the expected value");
    }

    @And("the verify login message should be {string}")
    public void the_verify_login_message_should_be(String expectedMessage) {
        Assertions.assertEquals(verifyLoginResponse.getMessage(), expectedMessage,
                "The verifyLogin body message does not match the expected value");
    }

    // High order: does not depend on the UI driver, just needs to run after the scenario ends.
    @After(value = "@auth and @api", order = 100)
    public void cleanupSeedAccount() {
        if (!seedAccountPendingCleanup) {
            return;
        }
        try {
            accountApiClient.deleteAccount(seedEmail, SEED_PASSWORD);
        } catch (RuntimeException cleanupException) {
            LOGGER.warn("Best-effort cleanup of seed user '{}' failed, it may remain orphaned: {}",
                    seedEmail, cleanupException.getMessage());
        } finally {
            seedAccountPendingCleanup = false;
        }
    }
}
