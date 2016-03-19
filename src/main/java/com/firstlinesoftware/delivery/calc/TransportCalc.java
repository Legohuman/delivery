package com.firstlinesoftware.delivery.calc;

import com.firstlinesoftware.delivery.dto.PaymentInfo;
import com.firstlinesoftware.delivery.dto.ProductInfo;
import com.firstlinesoftware.delivery.dto.SegmentInfo;
import com.firstlinesoftware.delivery.dto.TransportFnKey;
import com.firstlinesoftware.delivery.eval.api.Function;
import com.firstlinesoftware.delivery.eval.impl.EvalContextImpl;
import com.firstlinesoftware.delivery.storage.map.Storage;
import com.github.excelmapper.core.utils.Validate;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class TransportCalc extends AbstractCalc {

    public TransportCalc(Storage storage) {
        super(storage);
    }

    @Override
    public Double calculate(ProductInfo productInfo, PaymentInfo paymentInfo, SegmentInfo segmentInfo) {
        EvalContextImpl context = initContext(productInfo, paymentInfo);

        TransportFnKey fnKey = new TransportFnKey(segmentInfo.getFrom(), segmentInfo.getTo(), segmentInfo.getTransportType(), segmentInfo.getCompany());
        String transportFn = storage.maps().transportFunctions().get(fnKey);
        Function<Object> fn = context.fn(transportFn);
        Validate.notNull(fn, String.format("Can not find transport function %s", fnKey));

        return (Double) fn.eval(context);

    }
}
