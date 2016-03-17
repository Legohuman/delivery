package com.firstlinesoftware.delivery.misc.rates.dto;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class ExcelCityDto {
    public enum Fileds {
        code, name
    }

    private String code;
    private String name;

    public ExcelCityDto() {
    }

    public ExcelCityDto(String code, String name) {
        this.code = code;
        this.name = name;
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

    @Override
    public String toString() {
        return "ExcelImportRateDto{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
