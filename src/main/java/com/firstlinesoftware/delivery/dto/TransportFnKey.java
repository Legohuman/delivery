package com.firstlinesoftware.delivery.dto;

import com.firstlinesoftware.delivery.calc.route.api.TransportType;
import com.firstlinesoftware.delivery.calc.route.api.Vertex;
import com.firstlinesoftware.delivery.util.CityCodeUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * User: Legohuman
 * Date: 18/03/16
 */
public class TransportFnKey implements Serializable, Comparable<TransportFnKey> {
    @NotNull
    private final String origin;

    @NotNull
    private final String destination;

    @NotNull
    private final TransportType transportType;

    @NotNull
    private final String companyCode;

    public TransportFnKey(@NotNull Vertex origin, @NotNull Vertex destination, @NotNull TransportType transportType, @NotNull String companyCode) {
        this.origin = CityCodeUtil.padCityCode(origin.getCityCode());
        this.destination = CityCodeUtil.padCityCode(destination.getCityCode());
        this.transportType = transportType;
        this.companyCode = companyCode;
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
    public TransportType getTransportType() {
        return transportType;
    }

    @NotNull
    public String getCompanyCode() {
        return companyCode;
    }

    @Override
    public int compareTo(TransportFnKey o) {
        return (origin + ":" + destination + ":" + transportType + ":" + companyCode)
                .compareTo(o.origin + ":" + o.destination + ":" + o.transportType + ":" + o.companyCode);

    }

    @Override
    public String toString() {
        return origin + ":" + destination + ":" + transportType + ":" + companyCode;
    }
}
