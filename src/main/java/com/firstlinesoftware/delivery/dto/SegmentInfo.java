package com.firstlinesoftware.delivery.dto;

import org.tirnak.salesman.model.Vertex;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class SegmentInfo {

    private Vertex from;

    private Vertex to;

    private TransportType transportType;

    private String company;


    public SegmentInfo() {
    }


    public Vertex getFrom() {
        return from;
    }

    public void setFrom(Vertex from) {
        this.from = from;
    }

    public Vertex getTo() {
        return to;
    }

    public void setTo(Vertex to) {
        this.to = to;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
