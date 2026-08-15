package com.portfolio.ae.utils;

import org.testng.annotations.Test;

public class AssertionsTest {

    @Test
    public void assertTruePassesSilentlyWhenConditionIsTrue() {
        Assertions.assertTrue(true, "should pass");
    }

    @Test(expectedExceptions = AssertionError.class)
    public void assertTrueThrowsWhenConditionIsFalse() {
        Assertions.assertTrue(false, "should fail");
    }

    @Test
    public void assertContainsPassesWhenSubstringPresent() {
        Assertions.assertContains("Account Created!", "Created", "should contain 'Created'");
    }

    @Test(expectedExceptions = AssertionError.class)
    public void assertContainsThrowsWhenSubstringMissing() {
        Assertions.assertContains("Account Created!", "Deleted", "should not contain 'Deleted'");
    }
}
