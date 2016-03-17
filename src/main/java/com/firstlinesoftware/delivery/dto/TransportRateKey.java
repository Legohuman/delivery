package com.firstlinesoftware.delivery.dto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class TransportRateKey implements Serializable, Comparable<TransportRateKey> {

    public enum TransportType {
        road, rail, air, sea
    }

    @NotNull
    private final String origin;

    @NotNull
    private final String destination;

    @NotNull
    private final TransportType transportType;

    @NotNull
    private final String companyCode;

    @NotNull
    private final String prop;

    /**
     * One of variants of property value. Used with rates that depend on additional value like container type.
     */
    @Nullable
    private final String variant;

    public TransportRateKey(@NotNull String origin, @NotNull String destination, @NotNull TransportType transportType, @NotNull String companyCode, @NotNull String prop, @Nullable String variant) {
        this.origin = origin;
        this.destination = destination;
        this.transportType = transportType;
        this.companyCode = companyCode;
        this.prop = prop;
        this.variant = variant;
    }

    @NotNull
    public String getOrigin() {
        return origin;
    }

    @NotNull
    public String getDestination() {
        return destination;
    }

    @NotNull
    public TransportRateKey.TransportType getTransportType() {
        return transportType;
    }

    @NotNull
    public String getCompanyCode() {
        return companyCode;
    }

    @NotNull
    public String getProp() {
        return prop;
    }

    @Nullable
    public String getVariant() {
        return variant;
    }

    @Override
    public int compareTo(TransportRateKey o) {
        return (origin + ":" + destination + ":" + transportType + ":" + companyCode + ":" + prop + ":" + variant)
                .compareTo(o.origin + ":" + o.destination + ":" + o.transportType + ":" + o.companyCode + ":" + o.prop + ":" + o.variant);
    }

    @Override
    public String toString() {
        String str = origin + ":" + destination + ":" + transportType + ":" + companyCode + ":" + prop;
        if (variant != null) {
            str += ":" + variant;
        }
        return str;
    }
}
