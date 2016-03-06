package com.firstlinesoftware.delivery.eval.impl.fn;

import com.firstlinesoftware.delivery.eval.api.EvalContext;
import com.firstlinesoftware.delivery.eval.api.Expression;
import com.firstlinesoftware.delivery.eval.api.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public abstract class AbstractFunction<T> implements Function<T> {

    private final List<Expression> args = new ArrayList<>();

    public AbstractFunction() {
    }

    public AbstractFunction(List<? extends Expression> args) {
        this.args.addAll(args);
    }

    public List<Expression> arguments() {
        return new ArrayList<>(args);
    }

    public Function<T> arguments(Expression... args) {
        this.args.clear();
        this.args.addAll(Arrays.asList(args));
        return this;
    }

    private void clearArgs() {
        this.args.clear();
    }

    @Override
    public final T eval(EvalContext context) {
        T res = evaluate(context);
        clearArgs();
        return res;
    }

    public abstract T evaluate(EvalContext context);

}
