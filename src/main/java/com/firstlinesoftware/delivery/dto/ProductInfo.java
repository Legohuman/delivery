package com.firstlinesoftware.delivery.dto;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class ProductInfo {

    private Product product;

    private double cost;

    private double weight;

    private double percentOfTransportVolume;

    public ProductInfo() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPercentOfTransportVolume() {
        return percentOfTransportVolume;
    }

    public void setPercentOfTransportVolume(double percentOfTransportVolume) {
        this.percentOfTransportVolume = percentOfTransportVolume;
    }
}
