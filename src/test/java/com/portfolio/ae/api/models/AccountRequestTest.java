package com.portfolio.ae.api.models;

import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class AccountRequestTest {

    @Test
    public void toFormParamsUsesTheExactParamNamesExpectedByTheSite() {
        Map<String, String> formParams = AccountRequest.anAccount()
                .withName("Api User")
                .withEmail("api.user@mailinator.com")
                .withPassword("Passw0rd!123")
                .withFirstName("Api")
                .withLastName("User")
                .withAddress1("123 Test St")
                .withCity("Los Angeles")
                .withState("CA")
                .withZipcode("90001")
                .withMobileNumber("5551234567")
                .toFormParams();

        // Names confirmed against the real site: "birth_date" (not "birth_day"), "firstname"/
        // "lastname" (not "first_name"/"last_name"), "mobile_number".
        assertEquals(formParams.get("name"), "Api User");
        assertEquals(formParams.get("email"), "api.user@mailinator.com");
        assertEquals(formParams.get("password"), "Passw0rd!123");
        assertEquals(formParams.get("firstname"), "Api");
        assertEquals(formParams.get("lastname"), "User");
        assertEquals(formParams.get("birth_date"), "10");
        assertEquals(formParams.get("birth_month"), "5");
        assertEquals(formParams.get("birth_year"), "1990");
        assertEquals(formParams.get("mobile_number"), "5551234567");
    }

    @Test
    public void toCredentialsFormParamsOnlyIncludesEmailAndPassword() {
        Map<String, String> formParams = AccountRequest.anAccount()
                .withEmail("api.user@mailinator.com")
                .withPassword("Passw0rd!123")
                .toCredentialsFormParams();

        assertEquals(formParams.size(), 2);
        assertEquals(formParams.get("email"), "api.user@mailinator.com");
        assertEquals(formParams.get("password"), "Passw0rd!123");
    }
}
