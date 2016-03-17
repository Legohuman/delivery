package com.firstlinesoftware.delivery.dto;

import java.io.Serializable;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class City implements Serializable {

    private final Long code;

    private final String name;

    public City(Long code, String name) {
        this.code = code;
        this.name = name;
    }

    public Long getCode() {
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
                '}';
    }
}
