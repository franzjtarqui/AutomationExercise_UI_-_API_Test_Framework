package com.portfolio.ae.api.clients;

import com.portfolio.ae.api.models.AccountRequest;
import com.portfolio.ae.api.models.ApiResponse;
import com.portfolio.ae.api.models.UserDetailResponse;

import java.util.Map;

/**
 * Client for the Account resource (PLAN.md section 4/6): full lifecycle of an account via API
 * (create -> update -> getDetail -> delete), the basis of {@code account_lifecycle_api.feature}.
 */
public class AccountApiClient extends BaseApiClient {

    private static final String CREATE_ACCOUNT_PATH = "/createAccount";
    private static final String UPDATE_ACCOUNT_PATH = "/updateAccount";
    private static final String DELETE_ACCOUNT_PATH = "/deleteAccount";
    private static final String USER_DETAIL_BY_EMAIL_PATH = "/getUserDetailByEmail";

    public ApiResponse createAccount(AccountRequest account) {
        return bodyAs(post(CREATE_ACCOUNT_PATH, account.toFormParams()), ApiResponse.class);
    }

    public ApiResponse updateAccount(AccountRequest account) {
        return bodyAs(put(UPDATE_ACCOUNT_PATH, account.toFormParams()), ApiResponse.class);
    }

    public ApiResponse deleteAccount(String email, String password) {
        return bodyAs(delete(DELETE_ACCOUNT_PATH, Map.of("email", email, "password", password)), ApiResponse.class);
    }

    public UserDetailResponse getUserDetailByEmail(String email) {
        return bodyAs(get(USER_DETAIL_BY_EMAIL_PATH, Map.of("email", email)), UserDetailResponse.class);
    }
}
