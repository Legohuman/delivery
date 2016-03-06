package com.firstlinesoftware.delivery.eval.impl;

import com.firstlinesoftware.delivery.eval.api.EvalContext;
import com.firstlinesoftware.delivery.eval.api.Function;
import com.firstlinesoftware.delivery.eval.api.FunctionAlias;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public class FunctionAliasImpl<T> implements FunctionAlias<T> {

    private final String name;

    public FunctionAliasImpl(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public T eval(EvalContext context) {
        Function<T> fn = context.fn(name);
        if (fn == null) {
            throw new IllegalStateException(String.format("Function %s is not defined in evaluation context.", name));
        }
        return fn.eval(context);
    }
}
