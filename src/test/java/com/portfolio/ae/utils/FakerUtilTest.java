package com.portfolio.ae.utils;

import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

public class FakerUtilTest {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9.]+@mailinator\\.com$");

    @Test
    public void uniqueEmailMatchesExpectedFormat() {
        String email = FakerUtil.uniqueEmail();
        assertTrue(EMAIL_PATTERN.matcher(email).matches(), "Email has an unexpected format: " + email);
    }

    @Test
    public void uniqueEmailDoesNotCollideAcrossCalls() {
        String first = FakerUtil.uniqueEmail();
        String second = FakerUtil.uniqueEmail();
        assertNotEquals(first, second, "Two consecutive calls generated the same email");
    }

    @Test
    public void passwordRespectsMinimumLength() {
        String password = FakerUtil.password();
        assertTrue(password.length() >= 10, "Password is too short: " + password);
    }
}
