package com.firstlinesoftware.delivery.calc;

import com.firstlinesoftware.delivery.dto.ImportPaymentKey;
import com.firstlinesoftware.delivery.dto.PaymentInfo;
import com.firstlinesoftware.delivery.dto.ProductInfo;
import com.firstlinesoftware.delivery.dto.SegmentInfo;
import com.firstlinesoftware.delivery.eval.impl.EvalContextImpl;
import com.firstlinesoftware.delivery.eval.impl.ExpressionParserImpl;
import com.firstlinesoftware.delivery.storage.map.Storage;

import java.util.concurrent.ConcurrentNavigableMap;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class ImportProductCalc extends AbstractCalc {

    public ImportProductCalc(Storage storage) {
        super(storage);
    }

    @Override
    public Double calculate(ProductInfo productInfo, PaymentInfo paymentInfo, SegmentInfo segmentInfo) {
        EvalContextImpl context = initContext(productInfo, paymentInfo, segmentInfo);

        double customsServiceRate = 0.01;
        double customsService = productInfo.getCost() * customsServiceRate;

        ConcurrentNavigableMap<ImportPaymentKey, String> importPaymentRules = storage.maps().importPaymentRules();
        ImportPaymentKey key = new ImportPaymentKey(paymentInfo.getReceiver(), productInfo.getProduct().getCode());
        double customsImportSum = (Double) new ExpressionParserImpl().parse(importPaymentRules.get(key)).eval(context);

        return customsImportSum + customsService;
    }
}
