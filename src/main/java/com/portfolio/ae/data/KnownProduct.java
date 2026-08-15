package com.portfolio.ae.data;

/**
 * Stable reference product from automationexercise.com's real catalog (PLAN.md section 7),
 * used to avoid hardcoding loose name/price/category values in step definitions. The values come
 * from the fixture {@code testdata/known_products.json}.
 */
public record KnownProduct(String id, String name, String category, String price, String availability) {

    /** Extracts the numeric amount from a price formatted like "Rs. 500". */
    public int priceAmount() {
        return Integer.parseInt(price.replaceAll("[^0-9]", ""));
    }
}
