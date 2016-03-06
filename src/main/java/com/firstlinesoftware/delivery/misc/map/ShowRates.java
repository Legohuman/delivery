package com.firstlinesoftware.delivery.misc.map;

import com.firstlinesoftware.delivery.storage.map.Storage;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class ShowRates {
    public static void main(String[] args) {
        Storage storage = new Storage();
        storage.open();
        storage.maps().importRates().entrySet().forEach(e -> {
            System.out.format("%s: %s\n", e.getKey(), e.getValue());
        });
        storage.close();
    }
}
