package com.portfolio.ae.api.clients;

import com.portfolio.ae.api.models.ApiResponse;
import com.portfolio.ae.api.models.BrandsListResponse;

/**
 * Client for the Brands resource (PLAN.md section 4): full list of brands and the contract
 * check that this endpoint doesn't support {@code PUT}.
 */
public class BrandApiClient extends BaseApiClient {

    private static final String BRANDS_LIST_PATH = "/brandsList";

    public BrandsListResponse getBrandsList() {
        return bodyAs(get(BRANDS_LIST_PATH), BrandsListResponse.class);
    }

    /** Deliberate contract violation: only GET is supported (PLAN.md section 4, 405 expected). */
    public ApiResponse putBrandsList() {
        return bodyAs(put(BRANDS_LIST_PATH), ApiResponse.class);
    }
}
