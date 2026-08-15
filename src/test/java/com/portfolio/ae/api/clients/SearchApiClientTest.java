package com.portfolio.ae.api.clients;

import com.portfolio.ae.api.models.ApiResponse;
import com.portfolio.ae.api.models.ProductsListResponse;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class SearchApiClientTest {

    private final SearchApiClient client = new SearchApiClient();

    @Test
    public void searchProductReturnsMatchingProducts() {
        ProductsListResponse response = client.searchProduct("top");

        assertEquals(response.getResponseCode(), 200);
        // The site matches against name AND category (e.g. "Tops & Shirts"), not just an exact
        // substring of the name; that's why we only validate that there are results, not the exact content.
        assertFalse(response.getProducts().isEmpty(), "Searching for 'top' should return results");
    }

    @Test
    public void searchProductWithoutParamReturns400InTheBody() {
        ApiResponse response = client.searchProductWithoutParam();

        assertEquals(response.getResponseCode(), 400);
    }
}
