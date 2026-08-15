package com.portfolio.ae.data;

import com.portfolio.ae.utils.JsonReader;

import java.util.Map;

/**
 * Expected site texts (PLAN.md section 7), read once from the fixture
 * {@code testdata/expected_messages.json}. Centralizing them avoids repeating the same literals
 * across multiple step definition classes and makes it easier to update them if the site's copy changes.
 */
public final class ExpectedMessages {

    private static final Map<String, String> MESSAGES = JsonReader.readStringMap("testdata/expected_messages.json");

    private ExpectedMessages() {
    }

    public static String accountCreated() {
        return get("accountCreated");
    }

    public static String accountDeleted() {
        return get("accountDeleted");
    }

    public static String loginError() {
        return get("loginError");
    }

    public static String signupEmailExists() {
        return get("signupEmailExists");
    }

    public static String orderPlaced() {
        return get("orderPlaced");
    }

    public static String cartItemAdded() {
        return get("cartItemAdded");
    }

    private static String get(String key) {
        String value = MESSAGES.get(key);
        if (value == null) {
            throw new IllegalStateException("Expected message not found in fixture: '" + key + "'");
        }
        return value;
    }
}
