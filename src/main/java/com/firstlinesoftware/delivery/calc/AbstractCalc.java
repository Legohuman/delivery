package com.firstlinesoftware.delivery.calc;

import com.firstlinesoftware.delivery.dto.PaymentInfo;
import com.firstlinesoftware.delivery.dto.ProductInfo;
import com.firstlinesoftware.delivery.dto.SegmentInfo;
import com.firstlinesoftware.delivery.eval.impl.EvalContextImpl;
import com.firstlinesoftware.delivery.eval.impl.Variables;
import com.firstlinesoftware.delivery.eval.impl.fn.Functions;
import com.firstlinesoftware.delivery.storage.map.Storage;

/**
 * User: Legohuman
 * Date: 19/03/16
 */
public abstract class AbstractCalc implements PaymentCalc {

    protected final Storage storage;

    public AbstractCalc(Storage storage) {
        this.storage = storage;
    }


    protected EvalContextImpl initContext(ProductInfo productInfo, PaymentInfo paymentInfo, SegmentInfo segmentInfo) {
        EvalContextImpl context = new EvalContextImpl();

        Variables.initContext(context, productInfo, paymentInfo, segmentInfo);
        new RatesCalcHelper(storage).initContext(context);
        Functions.initContext(context);
        return context;
    }
}
