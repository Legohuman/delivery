package com.firstlinesoftware.delivery.dto;

import java.io.Serializable;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class City implements Serializable {

    private final int code;

    private final String name;

    private final String countryCode;

    public City(int code, String name, String countryCode) {
        this.code = code;
        this.name = name;
        this.countryCode = countryCode;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "City{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
