package com.portfolio.ae.api.clients;

import com.portfolio.ae.api.models.ApiResponse;
import com.portfolio.ae.api.models.ProductsListResponse;

/**
 * Client for the Products resource (PLAN.md section 4): full list of products and the contract
 * check that this endpoint doesn't support {@code POST}.
 */
public class ProductApiClient extends BaseApiClient {

    private static final String PRODUCTS_LIST_PATH = "/productsList";

    public ProductsListResponse getProductsList() {
        return bodyAs(get(PRODUCTS_LIST_PATH), ProductsListResponse.class);
    }

    /** Deliberate contract violation: only GET is supported (PLAN.md section 4, 405 expected). */
    public ApiResponse postProductsList() {
        return bodyAs(post(PRODUCTS_LIST_PATH), ApiResponse.class);
    }
}
