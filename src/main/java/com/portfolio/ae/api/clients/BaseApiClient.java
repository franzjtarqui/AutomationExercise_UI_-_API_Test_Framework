package com.portfolio.ae.api.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.ae.api.specs.RequestSpecFactory;
import com.portfolio.ae.api.specs.ResponseSpecFactory;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Base for the per-resource clients (PLAN.md section 6: {@code ProductApiClient},
 * {@code BrandApiClient}, {@code SearchApiClient}, {@code AuthApiClient}, {@code AccountApiClient}).
 * Centralizes the HTTP verbs against the common request/response spec; the concrete clients
 * expose business methods (e.g. {@code getProductsList()}) that delegate here and deserialize the
 * response into the corresponding {@code api.models} model.
 */
public abstract class BaseApiClient {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    protected Response get(String path) {
        return get(path, Collections.emptyMap());
    }

    protected Response get(String path, Map<String, ?> queryParams) {
        return requestSpec()
                .queryParams(queryParams)
                .get(path)
                .then().spec(ResponseSpecFactory.defaultResponseSpec())
                .extract().response();
    }

    /** No form params: used to exercise contract violations (e.g. POST /productsList -> 405). */
    protected Response post(String path) {
        return requestSpec()
                .post(path)
                .then().spec(ResponseSpecFactory.defaultResponseSpec())
                .extract().response();
    }

    protected Response post(String path, Map<String, ?> formParams) {
        return requestSpec()
                .formParams(formParams)
                .post(path)
                .then().spec(ResponseSpecFactory.defaultResponseSpec())
                .extract().response();
    }

    /** No form params: used to exercise contract violations (e.g. PUT /brandsList -> 405). */
    protected Response put(String path) {
        return requestSpec()
                .put(path)
                .then().spec(ResponseSpecFactory.defaultResponseSpec())
                .extract().response();
    }

    protected Response put(String path, Map<String, ?> formParams) {
        return requestSpec()
                .formParams(formParams)
                .put(path)
                .then().spec(ResponseSpecFactory.defaultResponseSpec())
                .extract().response();
    }

    /** No form params: used to exercise contract violations (e.g. DELETE /verifyLogin -> 405). */
    protected Response delete(String path) {
        return requestSpec()
                .delete(path)
                .then().spec(ResponseSpecFactory.defaultResponseSpec())
                .extract().response();
    }

    protected Response delete(String path, Map<String, ?> formParams) {
        return requestSpec()
                .formParams(formParams)
                .delete(path)
                .then().spec(ResponseSpecFactory.defaultResponseSpec())
                .extract().response();
    }

    private RequestSpecification requestSpec() {
        return RestAssured.given().spec(RequestSpecFactory.baseSpec());
    }

    /**
     * Deserializes {@code response}'s body into the given {@code targetType} with a custom
     * {@link ObjectMapper}, instead of using {@code response.as(Class)}. automationexercise.com
     * returns valid JSON but with {@code Content-Type: text/html} (confirmed against the real
     * site), and REST Assured's global parser registry ({@code RestAssured.registerParser})
     * turned out not to be reliable for forcing this (non-deterministic behavior documented in
     * the project's own issues). Parsing the body as plain text avoids depending on
     * {@code Content-Type} at all.
     */
    protected <T> T bodyAs(Response response, Class<T> targetType) {
        try {
            return JSON_MAPPER.readValue(response.getBody().asByteArray(), targetType);
        } catch (IOException ioException) {
            throw new IllegalStateException(
                    "Could not deserialize the response body (status=" + response.getStatusCode()
                            + ") as " + targetType.getSimpleName(),
                    ioException);
        }
    }
}
