package com.portfolio.ae.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User as returned by {@code GET /getUserDetailByEmail}. The response JSON's field names
 * ({@code birth_day}, {@code first_name}, ...) don't match what the {@code createAccount}/
 * {@code updateAccount} body expects ({@code birth_date}, {@code firstname}, with no
 * {@code mobile_number} in the response); that's why this response model lives separately from
 * {@link AccountRequest}, which represents the input payload.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {

    private int id;
    private String name;
    private String email;
    private String title;

    @JsonProperty("birth_day")
    private String birthDay;

    @JsonProperty("birth_month")
    private String birthMonth;

    @JsonProperty("birth_year")
    private String birthYear;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String company;
    private String address1;
    private String address2;
    private String country;
    private String state;
    private String city;
    private String zipcode;
}
