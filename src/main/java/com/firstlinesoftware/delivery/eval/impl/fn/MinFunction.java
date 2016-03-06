package com.firstlinesoftware.delivery.eval.impl.fn;

import com.firstlinesoftware.delivery.eval.api.Expression;

import java.util.List;
import java.util.OptionalDouble;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public class MinFunction extends AbstractReduceFunction<Double> {

    public MinFunction() {
    }

    public MinFunction(List<? extends Expression> args) {
        super(args);
    }

    @Override
    protected Double operation(List<Object> results) {
        OptionalDouble minOpt = results.stream().mapToDouble(
                t -> {
                    if (!(t instanceof Double)) {
                        throw new IllegalArgumentException(String.format("Value %s can not be converted to double", String.valueOf(t)));
                    }
                    return (Double) t;
                }).min();
        return minOpt.isPresent() ? minOpt.getAsDouble() : null;
    }
}
