package com.firstlinesoftware.delivery.calc;

import com.firstlinesoftware.delivery.dto.*;
import com.firstlinesoftware.delivery.eval.api.EvalContext;
import com.firstlinesoftware.delivery.eval.api.Expression;
import com.firstlinesoftware.delivery.eval.impl.Variables;
import com.firstlinesoftware.delivery.eval.impl.fn.AbstractFunction;
import com.firstlinesoftware.delivery.storage.map.Storage;
import com.github.excelmapper.core.utils.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.DoubleStream;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class RatesCalcHelper {

    public static final String importRateFnName = "importRate";
    public static final String containerTypeTransportValueFnName = "containerTypeTransportValueFnName";
    public static final String dimensionBasedTransportValueFnName = "dimensionBasedTransportValueFnName";

    private final Storage storage;

    public RatesCalcHelper(Storage storage) {
        this.storage = storage;
    }

    public void initContext(@NotNull EvalContext context) {
        context.fn(importRateFnName, new AbstractFunction<Double>() {

            @Override
            public Double evaluate(EvalContext context) {
                String receiver = context.var(Variables.payment_receiver.name());
                String paymentType = context.var(Variables.payment_type.name());
                String productCode = context.var(Variables.product_code.name());
                List<Expression> arguments = arguments();
                String productProp = (String) arguments.get(0).eval(context);
                if ("ru".equals(receiver)) {
                    if (PaymentInfo.PaymentType.imp.name().equals(paymentType)) {
                        return storage.maps().importRates().get(new ImportRateKey(receiver, productCode, productProp));
                    }
                }

                return null;
            }
        });

        context.fn(containerTypeTransportValueFnName, new AbstractFunction<Double>() {

            @Override
            public Double evaluate(EvalContext context) {
                PaymentInfo payment = context.var(Variables.payment.name());
                SegmentInfo segment = context.var(Variables.segment.name());
                return payment.getContainers().entrySet().stream()
                        .mapToDouble(e -> {
                            TransportRateKey rateKey = new TransportRateKey(segment.getFrom(), segment.getTo(),
                                    segment.getTransportType(), segment.getCompany(), RateProp.cntType.name(), e.getKey().toString());
                            return e.getValue() * storage.maps().transportRates().get(rateKey);
                        }).sum();
            }
        });

        context.fn(dimensionBasedTransportValueFnName, new AbstractFunction<Double>() {

            @Override
            public Double evaluate(EvalContext context) {
                PaymentInfo payment = context.var(Variables.payment.name());
                SegmentInfo segment = context.var(Variables.segment.name());
                Validate.notNull(payment.getProducts(), "Product list can not be null");

                TransportRateKey weightRateKey = new TransportRateKey(payment.getOrigin(), payment.getDestination(),
                        segment.getTransportType(), payment.getReceiver(), RateProp.weight.name(), null);
                Double weightRate = storage.maps().transportRates().get(weightRateKey);
                double weightCost = 0d;
                if (weightRate != null) {
                    double weight = payment.getProducts().stream().mapToDouble(ProductInfo::getWeight).sum();
                    weightCost = weight * weightRate;
                }

                TransportRateKey volumeRateKey = new TransportRateKey(payment.getOrigin(), payment.getDestination(),
                        segment.getTransportType(), payment.getReceiver(), RateProp.volume.name(), null);
                Double volumeRate = storage.maps().transportRates().get(volumeRateKey);
                double volumeCost = 0d;
                if (volumeRate != null) {
                    double volume = payment.getContainers().entrySet().stream().mapToDouble(e -> e.getKey().getVolume()).sum();
                    volumeCost = volume * weightRate;
                }

                TransportRateKey minCostRateKey = new TransportRateKey(payment.getOrigin(), payment.getDestination(),
                        segment.getTransportType(), payment.getReceiver(), RateProp.minCost.name(), null);
                Double minCostVal = storage.maps().transportRates().get(minCostRateKey);
                double minCost = minCostVal == null ? 0d : minCostVal;

                return DoubleStream.of(weightCost, volumeCost, minCost).max().getAsDouble();
            }
        });
    }
}
