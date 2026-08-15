package com.portfolio.ae.api.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

/**
 * automationexercise.com always responds with HTTP 200, even for contract violations: the
 * "real" code (400/404/405) travels in the body's {@code responseCode}, not in the HTTP status
 * (PLAN.md section 4/10, confirmed against the real site). That's why this spec only does a
 * minimal sanity check (200); the real contract is validated by deserializing the body against
 * the {@code api.models} models in the clients/step definitions.
 * <p>
 * {@code Content-Type} is not validated: the site returns valid JSON in the body but with header
 * {@code Content-Type: text/html} (confirmed against the real site), a known bug of this practice
 * API, not of the framework.
 */
public final class ResponseSpecFactory {

    private ResponseSpecFactory() {
    }

    public static ResponseSpecification defaultResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }
}
