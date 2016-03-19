package com.firstlinesoftware.delivery.misc.rates.dto;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class ExcelDimensionBasedTransportRateDto {
    public enum Fileds {
        fromCode, toCode, minCost, weightRate, volumeRate, duration
    }

    private String fromCode;
    private String toCode;
    private String minCost;
    private String weightRate;
    private String volumeRate;
    private String duration;

    public ExcelDimensionBasedTransportRateDto() {
    }

    public ExcelDimensionBasedTransportRateDto(String fromCode, String toCode, String minCost, String weightRate, String volumeRate, String duration) {
        this.fromCode = fromCode;
        this.toCode = toCode;
        this.minCost = minCost;
        this.weightRate = weightRate;
        this.volumeRate = volumeRate;
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

    public String getWeightRate() {
        return weightRate;
    }

    public void setWeightRate(String weightRate) {
        this.weightRate = weightRate;
    }

    public String getVolumeRate() {
        return volumeRate;
    }

    public void setVolumeRate(String volumeRate) {
        this.volumeRate = volumeRate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "ExcelDimensionBasedTransportRateDto{" +
                "fromCode='" + fromCode + '\'' +
                ", toCode='" + toCode + '\'' +
                ", minCost='" + minCost + '\'' +
                ", weightRate='" + weightRate + '\'' +
                ", volumeRate='" + volumeRate + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
