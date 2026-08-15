package com.portfolio.ae.api.clients;

import com.portfolio.ae.api.models.AccountRequest;
import com.portfolio.ae.api.models.ApiResponse;
import com.portfolio.ae.utils.FakerUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Uses its own "seed" account created via {@link AccountApiClient} (there is no persistent
 * reference user on the site) to exercise the valid credentials case; it is deleted in
 * {@link #deleteSeedAccount()}.
 */
public class AuthApiClientTest {

    private final AccountApiClient accountApiClient = new AccountApiClient();
    private final AuthApiClient authApiClient = new AuthApiClient();

    private AccountRequest seedAccount;

    @BeforeClass
    public void createSeedAccount() {
        seedAccount = AccountRequest.anAccount()
                .withName("Auth Api Seed")
                .withEmail(FakerUtil.uniqueEmail())
                .withPassword("Passw0rd!123")
                .withFirstName("Auth")
                .withLastName("Seed")
                .withAddress1("123 Seed St")
                .withCity("Seed City")
                .withState("CA")
                .withZipcode("90001")
                .withMobileNumber("5551234567");

        ApiResponse response = accountApiClient.createAccount(seedAccount);
        assertEquals(response.getResponseCode(), 201, "The seed account should be created before running the tests");
    }

    @AfterClass
    public void deleteSeedAccount() {
        accountApiClient.deleteAccount(seedAccount.getEmail(), seedAccount.getPassword());
    }

    @Test
    public void verifyLoginWithValidCredentialsReturns200() {
        ApiResponse response = authApiClient.verifyLogin(seedAccount.getEmail(), seedAccount.getPassword());

        assertEquals(response.getResponseCode(), 200);
        assertEquals(response.getMessage(), "User exists!");
    }

    @Test
    public void verifyLoginWithInvalidCredentialsReturns404() {
        ApiResponse response = authApiClient.verifyLogin(FakerUtil.uniqueEmail(), "wrong-password");

        assertEquals(response.getResponseCode(), 404);
    }

    @Test
    public void verifyLoginWithoutEmailReturns400() {
        ApiResponse response = authApiClient.verifyLoginWithoutEmail("some-password");

        assertEquals(response.getResponseCode(), 400);
    }

    @Test
    public void verifyLoginViaUnsupportedMethodReturns405() {
        ApiResponse response = authApiClient.verifyLoginViaUnsupportedMethod();

        assertEquals(response.getResponseCode(), 405);
    }
}
