package com.firstlinesoftware.delivery.dto;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class ImportRateKey implements Serializable, Comparable<ImportRateKey> {

    @NotNull
    private final String countryCode;

    @NotNull
    private final String productCode;

    @NotNull
    private final String productProp;

    public ImportRateKey(@NotNull String countryCode, @NotNull String productCode, @NotNull String productProp) {
        this.countryCode = countryCode;
        this.productCode = productCode;
        this.productProp = productProp;
    }

    @NotNull
    public String getCountryCode() {
        return countryCode;
    }

    @NotNull
    public String getProductCode() {
        return productCode;
    }

    @NotNull
    public String getProductProp() {
        return productProp;
    }

    @Override
    public int compareTo(ImportRateKey o) {
        if (this == o) return 0;
        return (countryCode + ":" + productCode + ":" + productProp)
                .compareTo(o.countryCode + ":" + o.productCode + ":" + o.productProp);
    }

    @Override
    public String toString() {
        return countryCode + ":" + productCode + ":" + productProp;
    }
}
