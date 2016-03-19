package com.firstlinesoftware.delivery.dto;

import java.io.Serializable;

/**
 * User: Legohuman
 * Date: 19/03/16
 */
public class SegmentVal implements Serializable {
    private final int from;
    private final int to;
    private final TransportType transportType;
    private final String company;

    public SegmentVal(int from, int to, TransportType transportType, String company) {
        this.from = from;
        this.to = to;
        this.transportType = transportType;
        this.company = company;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public String getCompany() {
        return company;
    }

    @Override
    public String toString() {
        return "SegmentVal{" +
                "from=" + from +
                ", to=" + to +
                ", transportType=" + transportType +
                ", company='" + company + '\'' +
                '}';
    }
}
