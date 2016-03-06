package com.firstlinesoftware.delivery.eval.impl.fn;

import com.firstlinesoftware.delivery.eval.api.Expression;

import java.util.List;
import java.util.OptionalDouble;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public class MaxFunction extends AbstractReduceFunction<Double> {

    public MaxFunction() {
    }

    public MaxFunction(List<? extends Expression> args) {
        super(args);
    }

    @Override
    protected Double operation(List<Object> results) {
        OptionalDouble maxOpt = results.stream().mapToDouble(
                t -> {
                    if (!(t instanceof Double)) {
                        throw new IllegalArgumentException(String.format("Value %s can not be converted to double", String.valueOf(t)));
                    }
                    return (Double) t;
                }).max();
        return maxOpt.isPresent() ? maxOpt.getAsDouble() : null;
    }
}
