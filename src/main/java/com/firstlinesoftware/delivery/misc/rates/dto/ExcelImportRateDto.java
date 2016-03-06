package com.firstlinesoftware.delivery.misc.rates.dto;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class ExcelImportRateDto {
    public enum Fileds{
        code, name, desc
    }

    private String code;
    private String name;
    private String desc;

    public ExcelImportRateDto() {
    }

    public ExcelImportRateDto(String code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ExcelImportRateDto{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
