package com.firstlinesoftware.delivery.misc.rates;

import com.firstlinesoftware.delivery.calc.RatesCalcHelper;
import com.firstlinesoftware.delivery.dto.*;
import com.firstlinesoftware.delivery.misc.rates.dto.ExcelContainerTypeTransportRateDto;
import com.firstlinesoftware.delivery.storage.map.Storage;
import com.firstlinesoftware.delivery.util.NumberParser;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentNavigableMap;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class ExcelFescoShipProcessor {


    public static final String transport_code_fesco = "fesco";
    private final Storage storage;

    private boolean initialized;
    private ConcurrentNavigableMap<TransportRateKey, Double> transportRates;
    private ConcurrentNavigableMap<TransportFnKey, String> transportFunctions;
    private ConcurrentNavigableMap<Integer, City> cities;
    private ConcurrentNavigableMap<Integer, SegmentVal> segments;


    public ExcelFescoShipProcessor(Storage storage) {
        this.storage = storage;
    }

    public void init() {
        if (initialized) {
            throw new IllegalStateException("ExcelFescoShipProcessor processor was already initialized");
        }
        transportRates = storage.maps().transportRates();
        transportFunctions = storage.maps().transportFunctions();
        cities = storage.maps().cities();
        segments = storage.maps().segments();
        initialized = true;
    }

    public void process(ExcelContainerTypeTransportRateDto dto) {
        if (!initialized) {
            throw new IllegalStateException("ExcelFescoShipProcessor processor was not initialized");
        }

        Integer from = NumberParser.parseInt(dto.getFromCode());
        Integer to = NumberParser.parseInt(dto.getToCode());
        String cntType = StringUtils.trimToNull(dto.getContainerType());
        Double cntRate = NumberParser.parseDouble(dto.getContainerRate());
        Double duration = NumberParser.parseDouble(dto.getDuration());


        if (to != null && from != null && cntType != null && cntRate != null && duration != null) {
            City fromCity = cities.get(from);
            City toCity = cities.get(to);
            if (fromCity != null && toCity != null) {
                transportRates.put(getRateKey(fromCity, toCity, cntType), cntRate);
                transportRates.put(getRateKey(fromCity, toCity, RateProp.duration), duration);

                transportFunctions.put(getFnKey(fromCity, toCity), RatesCalcHelper.containerTypeTransportValueFnName);

                int fromCode = fromCity.getCityCode();
                int toCode = toCity.getCityCode();
                segments.put(fromCode, new SegmentVal(fromCode, toCode, TransportType.sea, transport_code_fesco));
                segments.put(toCode, new SegmentVal(toCode, fromCode, TransportType.sea, transport_code_fesco));
            } else {
                System.out.format("City is not found from: %s to: %s fromCode: %s toCode: %s\n", fromCity, toCity, from, to);
            }
        } else {
            System.out.format("Not full row %s\n", dto);
        }
    }

    @NotNull
    private TransportRateKey getRateKey(City fromCity, City toCity, String variant) {
        return new TransportRateKey(fromCity, toCity, TransportType.sea, transport_code_fesco, RateProp.cntType.name(), variant);
    }

    @NotNull
    private TransportRateKey getRateKey(City fromCity, City toCity, RateProp rateProp) {
        return new TransportRateKey(fromCity, toCity, TransportType.sea, transport_code_fesco, rateProp.name(), null);
    }

    @NotNull
    private TransportFnKey getFnKey(City fromCity, City toCity) {
        return new TransportFnKey(fromCity, toCity, TransportType.sea, transport_code_fesco);
    }
}
