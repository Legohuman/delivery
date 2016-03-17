package com.firstlinesoftware.delivery.misc.rates.dto;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class ExcelContainerTypeTransportRateDto {
    public enum Fileds {
        fromCode, toCode, containerType, containerRate, duration
    }

    private String fromCode;
    private String toCode;
    private String minCost;
    private String containerType;
    private String containerRate;
    private String duration;

    public ExcelContainerTypeTransportRateDto() {
    }

    public ExcelContainerTypeTransportRateDto(String fromCode, String toCode, String minCost, String containerType, String containerRate, String duration) {
        this.fromCode = fromCode;
        this.toCode = toCode;
        this.minCost = minCost;
        this.containerType = containerType;
        this.containerRate = containerRate;
        this.duration = duration;
    }

    public String getFromCode() {
        return fromCode;
    }

    public void setFromCode(String fromCode) {
        this.fromCode = fromCode;
    }

    public String getToCode() {
        return toCode;
    }

    public void setToCode(String toCode) {
        this.toCode = toCode;
    }

    public String getMinCost() {
        return minCost;
    }

    public void setMinCost(String minCost) {
        this.minCost = minCost;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getContainerRate() {
        return containerRate;
    }

    public void setContainerRate(String containerRate) {
        this.containerRate = containerRate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
