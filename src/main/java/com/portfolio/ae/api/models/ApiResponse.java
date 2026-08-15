package com.portfolio.ae.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API response shape (PLAN.md section 4/6): {@code responseCode} + a text
 * {@code message}. Covers verifyLogin, createAccount, updateAccount, deleteAccount and the
 * contract error cases (400/404/405) of all endpoints, which the API always returns with a
 * real HTTP 200 and the real code inside the body.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private int responseCode;
    private String message;
}
