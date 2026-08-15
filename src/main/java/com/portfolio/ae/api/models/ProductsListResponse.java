package com.portfolio.ae.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response of {@code GET /productsList}, and also of {@code POST /searchProduct} when the
 * search finds results: both endpoints return exactly the same shape
 * {@code {responseCode, products}}, so this single model is reused for both.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsListResponse {

    private int responseCode;
    private List<Product> products;
}
