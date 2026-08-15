package com.portfolio.ae.api.specs;

import com.portfolio.ae.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Reusable base spec for all API clients (PLAN.md section 6): base URI from
 * {@link ConfigManager}, an Allure filter (attaches request/response to the report), and
 * automatic request+response logging only when a REST Assured validation fails (avoids noise
 * on green runs, gives full detail on failing ones).
 * <p>
 * Default Content-Type {@code application/x-www-form-urlencoded}: confirmed against the real
 * site that {@code createAccount}/{@code updateAccount} don't accept a JSON body (see
 * {@link com.portfolio.ae.api.models.AccountRequest}); GETs don't send a body so this
 * default doesn't affect them.
 * <p>
 * Note: the response body is deserialized with {@link com.portfolio.ae.api.clients.BaseApiClient#bodyAs}
 * (a custom {@code ObjectMapper}) instead of {@code Response.as(Class)}, because the site returns
 * valid JSON with {@code Content-Type: text/html} (confirmed against the real site) and REST
 * Assured's global parser registry turned out not to be reliable for forcing it.
 */
public final class RequestSpecFactory {

    private RequestSpecFactory() {
    }

    public static RequestSpecification baseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getBaseUrlApi())
                .setContentType(ContentType.URLENC.withCharset("UTF-8"))
                .setConfig(RestAssuredConfig.config()
                        .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails()))
                .addFilter(new AllureRestAssured())
                .build();
    }
}
