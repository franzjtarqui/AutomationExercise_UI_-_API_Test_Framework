package com.portfolio.ae.data;

import com.portfolio.ae.utils.FakerUtil;

/**
 * Builder for {@link AccountDetails} with default values generated via {@link FakerUtil}
 * (PLAN.md section 7): each field has a unique/valid default for the "Enter Account
 * Information" form, but any of them can be overridden with the {@code withX(...)} methods when a
 * scenario needs a specific value (e.g. a fixed country for a validation case).
 * <p>
 * The {@code company} and {@code address2} fields are optional on the real site and are left
 * {@code null} by default (not generated with Faker) so as not to compromise scenarios that verify
 * the behavior of empty fields.
 */
public class UserBuilder {

    private String title = "Mr";
    private String firstName = FakerUtil.firstName();
    private String lastName = FakerUtil.lastName();
    private String email = FakerUtil.uniqueEmail();
    private String password = FakerUtil.password();
    private String birthDay = "10";
    private String birthMonth = "May";
    private String birthYear = "1990";
    private String company;
    private String address1 = FakerUtil.addressLine();
    private String address2;
    private String country = "United States";
    private String state = FakerUtil.state();
    private String city = FakerUtil.city();
    private String zipcode = FakerUtil.zipCode();
    private String mobileNumber = FakerUtil.mobileNumber();

    public static UserBuilder aRandomUser() {
        return new UserBuilder();
    }

    public UserBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public UserBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withBirthDate(String day, String month, String year) {
        this.birthDay = day;
        this.birthMonth = month;
        this.birthYear = year;
        return this;
    }

    public UserBuilder withCompany(String company) {
        this.company = company;
        return this;
    }

    public UserBuilder withAddress1(String address1) {
        this.address1 = address1;
        return this;
    }

    public UserBuilder withAddress2(String address2) {
        this.address2 = address2;
        return this;
    }

    public UserBuilder withCountry(String country) {
        this.country = country;
        return this;
    }

    public UserBuilder withState(String state) {
        this.state = state;
        return this;
    }

    public UserBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public UserBuilder withZipcode(String zipcode) {
        this.zipcode = zipcode;
        return this;
    }

    public UserBuilder withMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public AccountDetails build() {
        String fullName = firstName + " " + lastName;
        return new AccountDetails(title, fullName, email, password, birthDay, birthMonth, birthYear,
                firstName, lastName, company, address1, address2, country, state, city, zipcode, mobileNumber);
    }
}
