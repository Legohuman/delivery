package com.firstlinesoftware.delivery.dto;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class PaymentInfo {

    public enum PaymentType {
        imp,
        exp,
        transport,
        transit
    }

    public enum ContainerType {
        с20ft,
        c40ft,
        none
    }

    private PaymentType type;

    private String receiver;

    private String originCityCode;

    private String destinationCityCode;

    private TransportRateKey.TransportType transportType;

    private LocalDate date;

    private Map<ContainerType, Integer> containers = new HashMap<>();

    public PaymentInfo() {
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getOriginCityCode() {
        return originCityCode;
    }

    public void setOriginCityCode(String originCityCode) {
        this.originCityCode = originCityCode;
    }

    public String getDestinationCityCode() {
        return destinationCityCode;
    }

    public void setDestinationCityCode(String destinationCityCode) {
        this.destinationCityCode = destinationCityCode;
    }

    public TransportRateKey.TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportRateKey.TransportType transportType) {
        this.transportType = transportType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setContainerCount(@NotNull ContainerType type, int count) {
        if (count <= 0) {
            containers.remove(type);
        } else {
            containers.put(type, count);
        }
    }

    public int getContainerCount(@NotNull ContainerType type) {
        Integer count = containers.get(type);
        return count == null ? 0 : count;
    }

    public Map<ContainerType, Integer> getContainers() {
        return new HashMap<>(containers);
    }
}
