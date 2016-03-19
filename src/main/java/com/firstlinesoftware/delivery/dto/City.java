package com.firstlinesoftware.delivery.dto;

import org.tirnak.salesman.model.Vertex;

import java.io.Serializable;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class City implements Serializable, Vertex {

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
    public int getCityCode() {
        return code;
    }

    @Override
    public String getCityName() {
        return name;
    }

    @Override
    public String getCountry() {
        return countryCode;
    }

    @Override
    public double fitness() {
        return 0; //unavailable here
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
