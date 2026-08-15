package com.portfolio.ae.data;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class UserBuilderTest {

    @Test
    public void aRandomUserHasAllRequiredFieldsPopulated() {
        AccountDetails accountDetails = UserBuilder.aRandomUser().build();

        assertEquals(accountDetails.title(), "Mr");
        assertEquals(accountDetails.country(), "United States");
        assertTrue(accountDetails.email().endsWith("@mailinator.com"), "Unexpected email: " + accountDetails.email());
        assertEquals(accountDetails.name(), accountDetails.firstName() + " " + accountDetails.lastName());
        assertNull(accountDetails.company(), "company should remain null by default (optional on the site)");
        assertNull(accountDetails.address2(), "address2 should remain null by default (optional on the site)");
    }

    @Test
    public void twoRandomUsersDoNotCollideOnEmail() {
        AccountDetails first = UserDataFactory.randomUser();
        AccountDetails second = UserDataFactory.randomUser();

        assertNotEquals(first.email(), second.email(), "Two random users generated the same email");
    }

    @Test
    public void withMethodsOverrideTheRandomDefaults() {
        AccountDetails accountDetails = UserDataFactory.randomUser(builder -> builder
                .withTitle("Mrs")
                .withCountry("India")
                .withCompany("Portfolio QA"));

        assertEquals(accountDetails.title(), "Mrs");
        assertEquals(accountDetails.country(), "India");
        assertEquals(accountDetails.company(), "Portfolio QA");
    }
}
