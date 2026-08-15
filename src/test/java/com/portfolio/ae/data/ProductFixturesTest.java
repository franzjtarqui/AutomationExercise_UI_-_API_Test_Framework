package com.portfolio.ae.data;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class ProductFixturesTest {

    @Test
    public void byIdReturnsTheKnownProduct() {
        KnownProduct product = ProductFixtures.byId("1");

        assertEquals(product.name(), "Blue Top");
        assertEquals(product.category(), "Tops");
        assertEquals(product.availability(), "In Stock");
        assertEquals(product.priceAmount(), 500);
    }

    @Test
    public void byIdThrowsForAnUnknownProduct() {
        assertThrows(IllegalArgumentException.class, () -> ProductFixtures.byId("does-not-exist"));
    }
}
