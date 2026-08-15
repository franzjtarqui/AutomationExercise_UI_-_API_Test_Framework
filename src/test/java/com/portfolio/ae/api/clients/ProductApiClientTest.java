package com.portfolio.ae.api.clients;

import com.portfolio.ae.api.models.ApiResponse;
import com.portfolio.ae.api.models.ProductsListResponse;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class ProductApiClientTest {

    private final ProductApiClient client = new ProductApiClient();

    @Test
    public void getProductsListReturnsTheFullCatalog() {
        ProductsListResponse response = client.getProductsList();

        assertEquals(response.getResponseCode(), 200);
        assertFalse(response.getProducts().isEmpty(), "The API should return at least one product");
    }

    @Test
    public void postProductsListReturns405InTheBody() {
        ApiResponse response = client.postProductsList();

        assertEquals(response.getResponseCode(), 405);
    }
}
