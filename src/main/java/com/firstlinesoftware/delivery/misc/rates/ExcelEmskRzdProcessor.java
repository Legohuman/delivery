package com.firstlinesoftware.delivery.misc.rates;

import com.firstlinesoftware.delivery.calc.RatesCalcHelper;
import com.firstlinesoftware.delivery.dto.*;
import com.firstlinesoftware.delivery.misc.rates.dto.ExcelDimensionBasedTransportRateDto;
import com.firstlinesoftware.delivery.storage.map.Storage;
import com.firstlinesoftware.delivery.util.NumberParser;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentNavigableMap;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class ExcelEmskRzdProcessor extends ExcelSegmentBasedProcessor {

    public static final String transport_code_emsk = "emsk";
    private final Storage storage;

    private boolean initialized;
    private ConcurrentNavigableMap<TransportRateKey, Double> transportRates;
    private ConcurrentNavigableMap<TransportFnKey, String> transportFunctions;
    private ConcurrentNavigableMap<Integer, City> cities;


    public ExcelEmskRzdProcessor(Storage storage) {
        this.storage = storage;
    }

    public void init() {
        if (initialized) {
            throw new IllegalStateException("ExcelEmskRzdProcessor processor was already initialized");
        }
        transportRates = storage.maps().transportRates();
        transportFunctions = storage.maps().transportFunctions();
        cities = storage.maps().cities();
        segments = storage.maps().segments();
        initialized = true;
    }

    public void process(ExcelDimensionBasedTransportRateDto dto) {
        if (!initialized) {
            throw new IllegalStateException("ExcelEmskRzdProcessor processor was not initialized");
        }

        Integer from = NumberParser.parseInt(dto.getFromCode());
        Integer to = NumberParser.parseInt(dto.getToCode());
        Double minCost = NumberParser.parseDouble(dto.getMinCost());
        Double weight = NumberParser.parseDouble(dto.getWeightRate());
        Double volume = NumberParser.parseDouble(dto.getVolumeRate());
        Double duration = NumberParser.parseDouble(dto.getDuration());


        if (to != null && from != null && minCost != null && weight != null && volume != null && duration != null) {
            City fromCity = cities.get(from);
            City toCity = cities.get(to);
            if (fromCity != null && toCity != null) {
                transportRates.put(getRateKey(fromCity, toCity, RateProp.minCost), minCost);
                transportRates.put(getRateKey(fromCity, toCity, RateProp.weight), weight);
                transportRates.put(getRateKey(fromCity, toCity, RateProp.volume), volume);
                transportRates.put(getRateKey(fromCity, toCity, RateProp.duration), duration);

                transportFunctions.put(getFnKey(fromCity, toCity), RatesCalcHelper.dimensionBasedTransportValueFnName);

                int fromCode = fromCity.getCityCode();
                int toCode = toCity.getCityCode();
                addSegment(fromCode, new SegmentVal(fromCode, toCode, TransportType.rail, transport_code_emsk));
                addSegment(toCode, new SegmentVal(toCode, fromCode, TransportType.rail, transport_code_emsk));
            } else {
                System.out.format("City is not found from: %s to: %s fromCode: %s toCode: %s\n", fromCity, toCity, from, to);
            }
        } else {
            System.out.format("Not full row %s\n", dto);
        }
    }

    @NotNull
    private TransportRateKey getRateKey(City fromCity, City toCity, RateProp rateProp) {
        return new TransportRateKey(fromCity, toCity, TransportType.rail, transport_code_emsk, rateProp.name(), null);
    }

    private @NotNull TransportFnKey getFnKey(City fromCity, City toCity) {
        return new TransportFnKey(fromCity, toCity, TransportType.rail, transport_code_emsk);
    }

}
