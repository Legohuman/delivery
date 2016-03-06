package com.firstlinesoftware.delivery.calc;

import com.firstlinesoftware.delivery.dto.ImportPaymentKey;
import com.firstlinesoftware.delivery.dto.PaymentInfo;
import com.firstlinesoftware.delivery.dto.ProductInfo;
import com.firstlinesoftware.delivery.eval.impl.EvalContextImpl;
import com.firstlinesoftware.delivery.eval.impl.ExpressionParserImpl;
import com.firstlinesoftware.delivery.eval.impl.Variables;
import com.firstlinesoftware.delivery.eval.impl.fn.Functions;
import com.firstlinesoftware.delivery.storage.map.Storage;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentNavigableMap;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class ImportProductCalc implements PaymentCalc {

    private final Storage storage;

    public ImportProductCalc(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Double calculate(ProductInfo productInfo, PaymentInfo paymentInfo) {
        EvalContextImpl context = initContext(productInfo, paymentInfo);

        double customsServiceRate = 0.01;
        double customsService = productInfo.getCost() * customsServiceRate;

        ConcurrentNavigableMap<ImportPaymentKey, String> importPaymentRules = storage.maps().importPaymentRules();
        ImportPaymentKey key = new ImportPaymentKey(paymentInfo.getReceiver(), productInfo.getProduct().getCode());
        double customsImportSum = (Double) new ExpressionParserImpl().parse(importPaymentRules.get(key)).eval(context);

        return customsImportSum + customsService;
    }

    @NotNull
    private EvalContextImpl initContext(ProductInfo productInfo, PaymentInfo paymentInfo) {
        EvalContextImpl context = new EvalContextImpl();

        Variables.initContext(context, productInfo, paymentInfo);
        new RatesCalcHelper(storage).initContext(context);
        Functions.initContext(context);
        return context;
    }
}
