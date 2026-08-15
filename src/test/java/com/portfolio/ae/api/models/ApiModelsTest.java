package com.portfolio.ae.api.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Deserialization of the models against sample JSON with the real shape of the API (captured
 * against the real site while designing this component), no network involved: validates the
 * field mapping (including the {@code @JsonProperty} of {@link UserAccount}) quickly and
 * deterministically.
 */
public class ApiModelsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void deserializesProductsListResponse() throws Exception {
        String json = "{\"responseCode\": 200, \"products\": [{\"id\": 1, \"name\": \"Blue Top\", "
                + "\"price\": \"Rs. 500\", \"brand\": \"Polo\", "
                + "\"category\": {\"usertype\": {\"usertype\": \"Women\"}, \"category\": \"Tops\"}}]}";

        ProductsListResponse response = objectMapper.readValue(json, ProductsListResponse.class);

        assertEquals(response.getResponseCode(), 200);
        assertEquals(response.getProducts().size(), 1);
        Product product = response.getProducts().get(0);
        assertEquals(product.getId(), 1);
        assertEquals(product.getName(), "Blue Top");
        assertEquals(product.getPrice(), "Rs. 500");
        assertEquals(product.getBrand(), "Polo");
        assertEquals(product.getCategory().getCategory(), "Tops");
        assertEquals(product.getCategory().getUsertype().getUsertype(), "Women");
    }

    @Test
    public void deserializesBrandsListResponse() throws Exception {
        String json = "{\"responseCode\": 200, \"brands\": [{\"id\": 1, \"brand\": \"Polo\"}]}";

        BrandsListResponse response = objectMapper.readValue(json, BrandsListResponse.class);

        assertEquals(response.getResponseCode(), 200);
        assertEquals(response.getBrands().get(0).getBrand(), "Polo");
    }

    @Test
    public void deserializesApiResponseMessage() throws Exception {
        String json = "{\"responseCode\": 405, \"message\": \"This request method is not supported.\"}";

        ApiResponse response = objectMapper.readValue(json, ApiResponse.class);

        assertEquals(response.getResponseCode(), 405);
        assertEquals(response.getMessage(), "This request method is not supported.");
    }

    @Test
    public void deserializesUserDetailResponseWithSnakeCaseFields() throws Exception {
        String json = "{\"responseCode\": 200, \"user\": {\"id\": 1, \"name\": \"Api User\", "
                + "\"email\": \"api.user@mailinator.com\", \"title\": \"Mr\", \"birth_day\": \"10\", "
                + "\"birth_month\": \"5\", \"birth_year\": \"1990\", \"first_name\": \"Api\", "
                + "\"last_name\": \"User\", \"company\": \"Portfolio QA\", \"address1\": \"123 Test St\", "
                + "\"address2\": \"\", \"country\": \"United States\", \"state\": \"CA\", "
                + "\"city\": \"Los Angeles\", \"zipcode\": \"90001\"}}";

        UserDetailResponse response = objectMapper.readValue(json, UserDetailResponse.class);

        assertEquals(response.getResponseCode(), 200);
        UserAccount user = response.getUser();
        assertEquals(user.getFirstName(), "Api");
        assertEquals(user.getLastName(), "User");
        assertEquals(user.getBirthDay(), "10");
        assertEquals(user.getBirthMonth(), "5");
        assertEquals(user.getBirthYear(), "1990");
        assertEquals(user.getCity(), "Los Angeles");
    }

    @Test
    public void ignoresUnknownFieldsInsteadOfFailing() throws Exception {
        String json = "{\"responseCode\": 200, \"products\": [], \"unexpectedField\": \"some value\"}";

        ProductsListResponse response = objectMapper.readValue(json, ProductsListResponse.class);

        assertEquals(response.getResponseCode(), 200);
        assertTrue(response.getProducts().isEmpty());
    }
}
