package com.portfolio.ae.api.models;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Payload for {@code POST /createAccount} and {@code PUT /updateAccount} (PLAN.md section 4/6).
 * Confirmed empirically against the real site: these endpoints do NOT accept a JSON body (an
 * attempt with {@code Content-Type: application/json} returns {@code "name parameter is missing"});
 * they only accept {@code application/x-www-form-urlencoded}. That's why this model isn't
 * deserialized with Jackson like the rest of {@code api.models}: it's a builder that gets
 * converted to a map of form params via {@link #toFormParams()}, with the exact parameter names
 * expected by the site (different from the ones {@link UserAccount} returns in the response).
 */
public class AccountRequest {

    private String name;
    private String email;
    private String password;
    private String title = "Mr";
    private String birthDate = "10";
    private String birthMonth = "5";
    private String birthYear = "1990";
    private String firstName;
    private String lastName;
    private String company = "";
    private String address1;
    private String address2 = "";
    private String country = "United States";
    private String zipcode;
    private String state;
    private String city;
    private String mobileNumber;

    public static AccountRequest anAccount() {
        return new AccountRequest();
    }

    public AccountRequest withName(String name) {
        this.name = name;
        return this;
    }

    public AccountRequest withEmail(String email) {
        this.email = email;
        return this;
    }

    public AccountRequest withPassword(String password) {
        this.password = password;
        return this;
    }

    public AccountRequest withTitle(String title) {
        this.title = title;
        return this;
    }

    public AccountRequest withBirthDate(String birthDate, String birthMonth, String birthYear) {
        this.birthDate = birthDate;
        this.birthMonth = birthMonth;
        this.birthYear = birthYear;
        return this;
    }

    public AccountRequest withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AccountRequest withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AccountRequest withCompany(String company) {
        this.company = company;
        return this;
    }

    public AccountRequest withAddress1(String address1) {
        this.address1 = address1;
        return this;
    }

    public AccountRequest withAddress2(String address2) {
        this.address2 = address2;
        return this;
    }

    public AccountRequest withCountry(String country) {
        this.country = country;
        return this;
    }

    public AccountRequest withZipcode(String zipcode) {
        this.zipcode = zipcode;
        return this;
    }

    public AccountRequest withState(String state) {
        this.state = state;
        return this;
    }

    public AccountRequest withCity(String city) {
        this.city = city;
        return this;
    }

    public AccountRequest withMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, String> toFormParams() {
        Map<String, String> formParams = new LinkedHashMap<>();
        formParams.put("name", name);
        formParams.put("email", email);
        formParams.put("password", password);
        formParams.put("title", title);
        formParams.put("birth_date", birthDate);
        formParams.put("birth_month", birthMonth);
        formParams.put("birth_year", birthYear);
        formParams.put("firstname", firstName);
        formParams.put("lastname", lastName);
        formParams.put("company", company);
        formParams.put("address1", address1);
        formParams.put("address2", address2);
        formParams.put("country", country);
        formParams.put("zipcode", zipcode);
        formParams.put("state", state);
        formParams.put("city", city);
        formParams.put("mobile_number", mobileNumber);
        return formParams;
    }

    /** Only {@code email} + {@code password}, the shape expected by deleteAccount/verifyLogin. */
    public Map<String, String> toCredentialsFormParams() {
        Map<String, String> formParams = new LinkedHashMap<>();
        formParams.put("email", email);
        formParams.put("password", password);
        return formParams;
    }
}
