package com.firstlinesoftware.delivery.calc;

import com.firstlinesoftware.delivery.dto.PaymentInfo;
import com.firstlinesoftware.delivery.dto.ProductInfo;
import com.firstlinesoftware.delivery.eval.api.EvalContext;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public interface PaymentCalc {

    Double calculate(ProductInfo productInfo, PaymentInfo paymentInfo);
}
