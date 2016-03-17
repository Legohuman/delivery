package com.firstlinesoftware.delivery.eval.impl.fn;

import com.firstlinesoftware.delivery.calc.RatesCalcHelper;
import com.firstlinesoftware.delivery.eval.api.EvalContext;
import com.firstlinesoftware.delivery.eval.api.FunctionAlias;
import com.firstlinesoftware.delivery.eval.impl.Variables;
import org.jetbrains.annotations.NotNull;

import static com.firstlinesoftware.delivery.eval.impl.EvalFactory.*;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public enum Functions {
    importMaxCostWeightRate,
    importWeightRate,
    importCostRate;

    public String alias() {
        return FunctionAlias.prefix + this.name();
    }

    public static void initContext(@NotNull EvalContext context) {
        AbstractFunction<Double> costRateFn = new AbstractFunction<Double>() {
            @Override
            public Double evaluate(EvalContext context) {
                return (Double) context.fn(RatesCalcHelper.importRateFnName).arguments(val("cost")).eval(context);
            }
        };
        AbstractFunction<Double> weightRateFn = new AbstractFunction<Double>() {
            @Override
            public Double evaluate(EvalContext context) {
                return (Double) context.fn(RatesCalcHelper.importRateFnName).arguments(val("weight")).eval(context);
            }
        };
        fnAlias(context, importCostRate.name(), mul(costRateFn, var(Variables.product_cost.name())));
        fnAlias(context, importWeightRate.name(), mul(weightRateFn, var(Variables.product_weight.name())));

        fnAlias(context, importMaxCostWeightRate.name(), max(
                mul(costRateFn, var(Variables.product_cost.name())),
                mul(weightRateFn, var(Variables.product_weight.name()))
        ));
    }
}
