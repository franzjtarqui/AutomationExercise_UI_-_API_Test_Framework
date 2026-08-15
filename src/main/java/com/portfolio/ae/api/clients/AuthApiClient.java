package com.portfolio.ae.api.clients;

import com.portfolio.ae.api.models.ApiResponse;

import java.util.Map;

/**
 * Client for the Auth / verifyLogin resource (PLAN.md section 4): credential verification and
 * contract checks (missing {@code email}, nonexistent credentials, unsupported method).
 */
public class AuthApiClient extends BaseApiClient {

    private static final String VERIFY_LOGIN_PATH = "/verifyLogin";

    public ApiResponse verifyLogin(String email, String password) {
        return bodyAs(post(VERIFY_LOGIN_PATH, Map.of("email", email, "password", password)), ApiResponse.class);
    }

    /** Deliberate contract violation: the {@code email} is missing (PLAN.md section 4, 400 expected). */
    public ApiResponse verifyLoginWithoutEmail(String password) {
        return bodyAs(post(VERIFY_LOGIN_PATH, Map.of("password", password)), ApiResponse.class);
    }

    /** Deliberate contract violation: the endpoint doesn't support DELETE (PLAN.md section 4, 405 expected). */
    public ApiResponse verifyLoginViaUnsupportedMethod() {
        return bodyAs(delete(VERIFY_LOGIN_PATH), ApiResponse.class);
    }
}
