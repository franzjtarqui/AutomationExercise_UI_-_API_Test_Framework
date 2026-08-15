package com.portfolio.ae.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.portfolio.ae.utils.JsonReader;

import java.util.List;

/**
 * Access to the reference products from the fixture {@code testdata/known_products.json}
 * (PLAN.md section 7). These IDs are stable in the site's real catalog (they are not created or
 * deleted at test time), unlike users, which are generated via {@link UserDataFactory}.
 */
public final class ProductFixtures {

    private static final List<KnownProduct> PRODUCTS = JsonReader.readList(
            "testdata/known_products.json", new TypeReference<List<KnownProduct>>() {
            });

    private ProductFixtures() {
    }

    public static KnownProduct byId(String id) {
        return PRODUCTS.stream()
                .filter(product -> product.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Known product not found in fixture: '" + id + "'"));
    }
}
