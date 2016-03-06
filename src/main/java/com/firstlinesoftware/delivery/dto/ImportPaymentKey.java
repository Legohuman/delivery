package com.firstlinesoftware.delivery.dto;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class ImportPaymentKey implements Serializable, Comparable<ImportPaymentKey> {

    @NotNull
    private final String countryCode;

    @NotNull
    private final String productCode;

    public ImportPaymentKey(@NotNull String countryCode, @NotNull String productCode) {
        this.countryCode = countryCode;
        this.productCode = productCode;
    }

    @NotNull
    public String getCountryCode() {
        return countryCode;
    }

    @NotNull
    public String getProductCode() {
        return productCode;
    }

    @Override
    public int compareTo(ImportPaymentKey o) {
        if (this == o) return 0;
        return (countryCode + ":" + productCode)
                .compareTo(o.countryCode + ":" + o.productCode);
    }

    @Override
    public String toString() {
        return countryCode + ":" + productCode;
    }
}
