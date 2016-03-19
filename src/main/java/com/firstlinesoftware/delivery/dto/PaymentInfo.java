package com.firstlinesoftware.delivery.dto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tirnak.salesman.model.Vertex;

import java.time.LocalDate;
import java.util.*;

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

    public enum FitnessProperty {
        time,
        money
    }

    private FitnessProperty fitnessProperty;

    private PaymentType type;

    private String receiver;

    private Vertex origin;

    private Vertex destination;

    private TransportType transportType;

    private LocalDate date;

    private Map<ContainerType, Integer> containers = new HashMap<>();

    @Nullable
    private List<ProductInfo> products;

    public PaymentInfo() {
    }

    public FitnessProperty getFitnessProperty() {
        return fitnessProperty;
    }

    public void setFitnessProperty(FitnessProperty fitnessProperty) {
        this.fitnessProperty = fitnessProperty;
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

    public Vertex getOrigin() {
        return origin;
    }

    public void setOrigin(Vertex origin) {
        this.origin = origin;
    }

    public Vertex getDestination() {
        return destination;
    }

    public void setDestination(Vertex destination) {
        this.destination = destination;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
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

    @Nullable
    public List<ProductInfo> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void setProducts(@Nullable List<ProductInfo> products) {
        this.products = new ArrayList<>(products);
    }
}
