package com.portfolio.ae.data;

/**
 * Full data for the "Enter Account Information" form (PLAN.md section 3, registration).
 * It's a simple DTO; construction with dynamic values via Faker lives in the
 * "Data layer" component (UserBuilder/UserDataFactory). Nullable fields (company, address2) are
 * optional on the site's real form.
 */
public record AccountDetails(
        String title,
        String name,
        String email,
        String password,
        String birthDay,
        String birthMonth,
        String birthYear,
        String firstName,
        String lastName,
        String company,
        String address1,
        String address2,
        String country,
        String state,
        String city,
        String zipcode,
        String mobileNumber
) {
}
