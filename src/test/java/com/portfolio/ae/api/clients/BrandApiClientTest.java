package com.portfolio.ae.api.clients;

import com.portfolio.ae.api.models.ApiResponse;
import com.portfolio.ae.api.models.BrandsListResponse;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class BrandApiClientTest {

    private final BrandApiClient client = new BrandApiClient();

    @Test
    public void getBrandsListReturnsTheFullCatalog() {
        BrandsListResponse response = client.getBrandsList();

        assertEquals(response.getResponseCode(), 200);
        assertFalse(response.getBrands().isEmpty(), "The API should return at least one brand");
    }

    @Test
    public void putBrandsListReturns405InTheBody() {
        ApiResponse response = client.putBrandsList();

        assertEquals(response.getResponseCode(), 405);
    }
}
