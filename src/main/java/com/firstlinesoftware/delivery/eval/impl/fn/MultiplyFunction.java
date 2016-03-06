package com.firstlinesoftware.delivery.eval.impl.fn;

import com.firstlinesoftware.delivery.eval.api.Expression;

import java.util.List;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public class MultiplyFunction extends AbstractReduceFunction<Double> {

    public MultiplyFunction() {
    }

    public MultiplyFunction(List<Expression> args) {
        super(args);
    }

    @Override
    protected Double operation(List<Object> results) {
        return results.stream().mapToDouble(
                t -> {
                    if (!(t instanceof Double)) {
                        throw new IllegalArgumentException(String.format("Value %s can not be converted to double", String.valueOf(t)));
                    }
                    return (Double) t;
                }).reduce(1d, (left, right) -> left * right);
    }
}
