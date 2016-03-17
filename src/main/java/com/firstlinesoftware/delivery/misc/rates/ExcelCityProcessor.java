package com.firstlinesoftware.delivery.misc.rates;

import com.firstlinesoftware.delivery.dto.City;
import com.firstlinesoftware.delivery.misc.rates.dto.ExcelCityDto;
import com.firstlinesoftware.delivery.storage.map.Storage;
import com.firstlinesoftware.delivery.util.NumberParser;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentNavigableMap;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class ExcelCityProcessor {


    private final Storage storage;

    private boolean initialized;
    private ConcurrentNavigableMap<Long, City> cities;


    public ExcelCityProcessor(Storage storage) {
        this.storage = storage;
    }

    public void init() {
        if (initialized) {
            throw new IllegalStateException("City processor was already initialized");
        }
        cities = storage.maps().cities();
        initialized = true;
    }

    public void process(ExcelCityDto dto) {
        if (!initialized) {
            throw new IllegalStateException("City processor was not initialized");
        }

        Long code = NumberParser.parseLong(dto.getCode());
        String name = StringUtils.trimToEmpty(dto.getName());
        int commentStartTokenIndex = name.indexOf('(');
        if (commentStartTokenIndex > -1) {
            name = StringUtils.trimToNull(name.substring(0, commentStartTokenIndex));
        }

        if (code != null && name != null) {
            City resultDto = new City(code, name);
            cities.put(code, resultDto);
            System.out.println(resultDto);
        } else {
            System.out.format("City with name %s has empty code", name);
        }
    }

}
