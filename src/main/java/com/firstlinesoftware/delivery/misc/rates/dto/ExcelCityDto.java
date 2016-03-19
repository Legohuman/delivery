package com.firstlinesoftware.delivery.misc.rates.dto;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class ExcelCityDto {
    public enum Fileds {
        code, name, countryCode
    }

    private String code;
    private String name;
    private String countryCode;

    public ExcelCityDto() {
    }

    public ExcelCityDto(String code, String name, String countryCode) {
        this.code = code;
        this.name = name;
        this.countryCode = countryCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return "ExcelCityDto{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
