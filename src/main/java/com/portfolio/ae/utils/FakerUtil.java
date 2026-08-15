package com.portfolio.ae.utils;

import net.datafaker.Faker;

import java.util.UUID;

/**
 * Unique dynamic data (PLAN.md section 7): avoids email collisions across repeated
 * and parallel runs. {@link Faker} is not thread-safe (it shares an internal {@code Random}),
 * so an instance is kept per thread.
 */
public final class FakerUtil {

    private static final ThreadLocal<Faker> FAKER = ThreadLocal.withInitial(Faker::new);

    private FakerUtil() {
    }

    private static Faker faker() {
        return FAKER.get();
    }

    /**
     * Email unique per invocation (name + random suffix), intended for user registrations
     * that must not collide across runs or across parallel scenarios.
     */
    public static String uniqueEmail() {
        String localPart = faker().name().firstName().toLowerCase()
                + "." + faker().name().lastName().toLowerCase()
                + "." + UUID.randomUUID().toString().substring(0, 8);
        return localPart.replaceAll("[^a-z0-9.]", "") + "@mailinator.com";
    }

    public static String firstName() {
        return faker().name().firstName();
    }

    public static String lastName() {
        return faker().name().lastName();
    }

    public static String password() {
        return faker().internet().password(10, 16, true, true, true);
    }

    public static String companyName() {
        return faker().company().name();
    }

    public static String addressLine() {
        return faker().address().streetAddress();
    }

    public static String city() {
        return faker().address().city();
    }

    public static String state() {
        return faker().address().state();
    }

    public static String zipCode() {
        return faker().address().zipCode();
    }

    public static String mobileNumber() {
        return faker().phoneNumber().cellPhone();
    }
}
