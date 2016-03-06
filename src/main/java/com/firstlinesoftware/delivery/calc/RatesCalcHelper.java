package com.firstlinesoftware.delivery.calc;

import com.firstlinesoftware.delivery.dto.ImportRateKey;
import com.firstlinesoftware.delivery.dto.PaymentInfo;
import com.firstlinesoftware.delivery.eval.api.EvalContext;
import com.firstlinesoftware.delivery.eval.api.Expression;
import com.firstlinesoftware.delivery.eval.impl.Variables;
import com.firstlinesoftware.delivery.eval.impl.fn.AbstractFunction;
import com.firstlinesoftware.delivery.storage.map.Storage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class RatesCalcHelper {

    public static final String rateFnName = "rate";

    private final Storage storage;

    public RatesCalcHelper(Storage storage) {
        this.storage = storage;
    }

    public void initContext(@NotNull EvalContext context) {
        context.fn(rateFnName, new AbstractFunction<Double>() {

            @Override
            public Double evaluate(EvalContext context) {
                String receiver = context.var(Variables.payment_receiver.name());
                String paymentType = context.var(Variables.payment_type.name());
                String productCode = context.var(Variables.product_code.name());
                List<Expression> arguments = arguments();
                String productProp = (String)arguments.get(0).eval(context);
                if("ru".equals(receiver)){
                    if(PaymentInfo.PaymentType.imp.name().equals(paymentType)){
                        return storage.maps().importRates().get(new ImportRateKey(receiver, productCode, productProp));
                    }
                }

                return null;
            }
        });
    }
}
