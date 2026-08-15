package com.portfolio.ae.api.clients;

import com.portfolio.ae.api.models.ApiResponse;
import com.portfolio.ae.api.models.ProductsListResponse;

import java.util.Map;

/**
 * Client for the Search resource (PLAN.md section 4): product search by name and the contract
 * check that the {@code search_product} parameter is required.
 */
public class SearchApiClient extends BaseApiClient {

    private static final String SEARCH_PRODUCT_PATH = "/searchProduct";

    public ProductsListResponse searchProduct(String searchTerm) {
        return bodyAs(post(SEARCH_PRODUCT_PATH, Map.of("search_product", searchTerm)), ProductsListResponse.class);
    }

    /** Deliberate contract violation: the required parameter is missing (PLAN.md section 4, 400 expected). */
    public ApiResponse searchProductWithoutParam() {
        return bodyAs(post(SEARCH_PRODUCT_PATH), ApiResponse.class);
    }
}
