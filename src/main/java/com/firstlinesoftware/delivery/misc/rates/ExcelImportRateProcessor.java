package com.firstlinesoftware.delivery.misc.rates;

import com.firstlinesoftware.delivery.dto.ImportPaymentKey;
import com.firstlinesoftware.delivery.dto.ImportRateKey;
import com.firstlinesoftware.delivery.dto.Product;
import com.firstlinesoftware.delivery.eval.impl.fn.Functions;
import com.firstlinesoftware.delivery.misc.rates.dto.ExcelImportRateDto;
import com.firstlinesoftware.delivery.storage.map.Storage;
import com.firstlinesoftware.delivery.util.NumberParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class ExcelImportRateProcessor {

    public static final String countryCode = "ru";
    public static final String propWeight = "weight";
    public static final String propCost = "cost";
    public static final String remarkSuffix = "С)";
    private final Storage storage;
    private AtomicInteger counter = new AtomicInteger(0);

    private boolean initialized;
    private ConcurrentNavigableMap<String, Product> products;
    private ConcurrentNavigableMap<ImportRateKey, Double> importRates;
    private ConcurrentNavigableMap<ImportPaymentKey, String> importPaymentRules;
    public static final String unitKgSuffix = "кг";
    public static final String unitLiterSuffix = "л";
    public static final String maxClause = ", но не менее";

    private static Map<String, String> replacements = initReplacementsMap();

    private static Map<String, String> initReplacementsMap() {
        HashMap<String, String> replacements = new HashMap<>();
        replacements.put("\u00A0", " ");
        replacements.put("a", "а");
        replacements.put("c", "с");
        replacements.put("e", "е");
        replacements.put("k", "к");
        replacements.put("o", "о");
        replacements.put("p", "р");
        replacements.put("x", "х");
        return replacements;
    }

    public ExcelImportRateProcessor(Storage storage) {
        this.storage = storage;
    }

    public void init() {
        if (initialized) {
            throw new IllegalStateException("Import rate processor was already initialized");
        }
        products = storage.maps().products();
        importRates = storage.maps().importRates();
        importPaymentRules = storage.maps().importPaymentRules();
        initialized = true;
    }

    public void process(ExcelImportRateDto dto) {
        if (!initialized) {
            throw new IllegalStateException("Import rate processor was not initialized");
        }
        counter.incrementAndGet();

        String code = dto.getCode();
        String name = dto.getName();
        String desc = dto.getDesc();
        if (!StringUtils.isBlank(code)) {
            products.put(code, new Product(code, name));

            ImmutablePair<String, List<ImmutablePair<ImportRateKey, Double>>> rateDesc = getRatePairs(code, processDesc(desc));
            List<ImmutablePair<ImportRateKey, Double>> ratePairs = rateDesc.getRight();
            if (ratePairs != null) {
                ratePairs.forEach(p -> importRates.put(p.getKey(), p.getValue()));
                importPaymentRules.put(new ImportPaymentKey(countryCode, code), rateDesc.getLeft());
            }
            System.out.println(dto);
        }
    }

    private String processDesc(@Nullable String desc) {
        if (desc != null) {
            String[] descArr = new String[]{desc};
            replacements.forEach((k, v) -> descArr[0] = descArr[0].replace(k, v));
            desc = descArr[0];
        }
        return desc;
    }

    @NotNull
    private ImmutablePair<String, List<ImmutablePair<ImportRateKey, Double>>> getRatePairs(@NotNull String code, @Nullable String desc) {
        String trimmed = StringUtils.trimToEmpty(desc);
        int maxClauseIndex = trimmed.indexOf(maxClause);
        if (maxClauseIndex != -1) {
            return new ImmutablePair<>(Functions.importMaxCostWeightRate.alias(), nullOrNotNullList(
                    getCostRate(code, trimmed.substring(0, maxClauseIndex)),
                    getWeightRate(code, trimmed.substring(maxClauseIndex + maxClause.length()))));
        } else if (trimmed.endsWith(unitKgSuffix) || trimmed.contentEquals(unitLiterSuffix)) {
            return new ImmutablePair<>(Functions.importWeightRate.alias(), nullOrSingletonList(getWeightRate(code, trimmed)));
        } else if (trimmed.endsWith(remarkSuffix)) {
            return new ImmutablePair<>(Functions.importCostRate.alias(), nullOrSingletonList(getCostRate(code, trimmed.substring(0, trimmed.length() - (remarkSuffix.length() + 1)))));
        } else {
            return new ImmutablePair<>(Functions.importCostRate.alias(), nullOrSingletonList(getCostRate(code, trimmed)));
        }
    }

    @Nullable
    private ImmutablePair<ImportRateKey, Double> getCostRate(@NotNull String code, @NotNull String costRatePart) {
        Double rate = NumberParser.parseDouble(costRatePart);
        return rate == null ? null : costPair(code, rate / 100); //convert percents to factor
    }

    @Nullable
    private ImmutablePair<ImportRateKey, Double> getWeightRate(@NotNull String code, @NotNull String weightRatePart) {
        String forStr = "за";
        String euroStr = "евро";

        if (weightRatePart.endsWith(unitKgSuffix) || weightRatePart.contentEquals(unitLiterSuffix)) {
            int forStrIndex = weightRatePart.indexOf(forStr);
            if (forStrIndex != -1) {
                String ratePart = StringUtils.trimToEmpty(weightRatePart.substring(0, forStrIndex));
                int euroStrIndex = ratePart.indexOf(euroStr);
                if (euroStrIndex != -1) {
                    Double rate = NumberParser.parseDouble(StringUtils.trimToEmpty(ratePart.substring(0, euroStrIndex)));

                    int lastSpace = weightRatePart.lastIndexOf(' ');
                    Double weight = NumberParser.parseDouble(weightRatePart.substring(forStrIndex + forStr.length(), lastSpace));
                    if (rate != null && weight != null) {
                        rate /= weight;
                        return weightPair(code, rate);
                    }
                }
            }
        }
        return null;
    }

    private <T> List<T> nullOrNotNullList(T... items) {
        List<T> list = Arrays.asList(items).stream().filter(t -> t != null).collect(Collectors.toList());
        return list == null ? null : list;
    }

    private <T> List<T> nullOrSingletonList(T item) {
        return item == null ? null : Collections.singletonList(item);
    }

    private ImmutablePair<ImportRateKey, Double> weightPair(@NotNull String code, @NotNull Double rate) {
        return propPair(code, propWeight, rate);
    }

    private ImmutablePair<ImportRateKey, Double> costPair(@NotNull String code, @NotNull Double rate) {
        return propPair(code, propCost, rate);
    }

    private ImmutablePair<ImportRateKey, Double> propPair(@NotNull String code, @NotNull String property, @NotNull Double rate) {
        return new ImmutablePair<>(new ImportRateKey(countryCode, code, property), rate);
    }

    public int processedCount() {
        return counter.get();
    }
}
