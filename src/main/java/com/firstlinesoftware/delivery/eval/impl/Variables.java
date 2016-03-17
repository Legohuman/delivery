package com.firstlinesoftware.delivery.eval.impl;

import com.firstlinesoftware.delivery.dto.PaymentInfo;
import com.firstlinesoftware.delivery.dto.ProductInfo;
import com.firstlinesoftware.delivery.eval.api.EvalContext;
import com.firstlinesoftware.delivery.eval.api.Variable;
import org.jetbrains.annotations.NotNull;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public enum Variables {
    product,
    product_code,
    product_cost,
    product_weight,
    product_ptv,
    payment,
    payment_type,
    payment_receiver;

    public String alias() {
        return Variable.prefix + this.name();
    }

    public static void initContext(@NotNull EvalContext context, @NotNull ProductInfo productInfo, @NotNull PaymentInfo paymentInfo) {
        context.var(product.name(), productInfo.getProduct());
        context.var(product_code.name(), productInfo.getProduct().getCode());
        context.var(product_cost.name(), productInfo.getCost());
        context.var(product_weight.name(), productInfo.getWeight());
        context.var(product_ptv.name(), productInfo.getPercentOfTransportVolume());
        context.var(payment.name(), paymentInfo);
        context.var(payment_type.name(), paymentInfo.getType().name());
        context.var(payment_receiver.name(), paymentInfo.getReceiver());
    }
}
