package com.firstlinesoftware.delivery.eval.impl.fn;

import com.firstlinesoftware.delivery.eval.api.EvalContext;
import com.firstlinesoftware.delivery.eval.api.Expression;
import com.firstlinesoftware.delivery.eval.api.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public abstract class AbstractReduceFunction<T> extends AbstractFunction<T> {

    public AbstractReduceFunction() {
    }

    public AbstractReduceFunction(List<? extends Expression> args) {
        super(args);
    }

    public T evaluate(EvalContext context) {
        return operation(arguments().stream().map(expr -> expr.eval(context)).collect(Collectors.toList()));
    }

    protected abstract T operation(List<Object> results);
}
