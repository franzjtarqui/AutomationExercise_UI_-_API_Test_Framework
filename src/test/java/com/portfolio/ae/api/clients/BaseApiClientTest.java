package com.portfolio.ae.api.clients;

import com.portfolio.ae.api.models.ApiResponse;
import com.portfolio.ae.api.models.ProductsListResponse;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * Minimal integration test against the real site: validates that the full plumbing of the API
 * layer base (baseUri via {@code ConfigManager}, Allure filter, response spec, deserialization
 * into {@code api.models} models via {@link BaseApiClient#bodyAs}) works end to end, without yet
 * needing a concrete client per resource (Component 12). {@code TestApiClient} is a minimal stub
 * just to expose the {@code protected} methods of {@link BaseApiClient} in the test.
 */
public class BaseApiClientTest {

    private static class TestApiClient extends BaseApiClient {
        Response getProductsList() {
            return get("/productsList");
        }

        Response postProductsList() {
            return post("/productsList");
        }

        Response postSearchProduct(Map<String, ?> formParams) {
            return post("/searchProduct", formParams);
        }

        <T> T parse(Response response, Class<T> targetType) {
            return bodyAs(response, targetType);
        }
    }

    private final TestApiClient client = new TestApiClient();

    @Test
    public void getDeserializesIntoTheProductsListModel() {
        ProductsListResponse response = client.parse(client.getProductsList(), ProductsListResponse.class);

        assertEquals(response.getResponseCode(), 200);
        assertFalse(response.getProducts().isEmpty(), "The API should return at least one product");
    }

    /** The site always responds with HTTP 200; the real contract code travels in the body. */
    @Test
    public void postWithoutFormParamsReturnsA405InTheBody() {
        Response response = client.postProductsList();

        assertEquals(response.getStatusCode(), 200);
        ApiResponse body = client.parse(response, ApiResponse.class);
        assertEquals(body.getResponseCode(), 405);
    }

    @Test
    public void postWithFormParamsReachesTheEndpointCorrectly() {
        Response response = client.postSearchProduct(Map.of("search_product", "top"));

        ProductsListResponse body = client.parse(response, ProductsListResponse.class);
        assertEquals(body.getResponseCode(), 200);
        assertFalse(body.getProducts().isEmpty());
    }
}
