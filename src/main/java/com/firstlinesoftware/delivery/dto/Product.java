package com.firstlinesoftware.delivery.dto;

import java.io.Serializable;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class Product implements Serializable {

    private final String code;

    private final String name;

    public Product(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
