package com.portfolio.ae.data;

import java.util.function.UnaryOperator;

/**
 * Simple entry point for obtaining test {@link AccountDetails} (PLAN.md section 7).
 * For the common case (random user with no particularities), {@link #randomUser()} is enough;
 * when a scenario needs to customize some field, {@link #randomUser(UnaryOperator)}
 * exposes the underlying {@link UserBuilder} without the step definition having to know its
 * defaults.
 */
public final class UserDataFactory {

    private UserDataFactory() {
    }

    public static AccountDetails randomUser() {
        return UserBuilder.aRandomUser().build();
    }

    public static AccountDetails randomUser(UnaryOperator<UserBuilder> customization) {
        return customization.apply(UserBuilder.aRandomUser()).build();
    }
}
