package com.firstlinesoftware.delivery.eval.impl;

import com.firstlinesoftware.delivery.eval.api.EvalContext;
import com.firstlinesoftware.delivery.eval.api.Variable;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public class VariableImpl<T> implements Variable<T>{

    private final String name;

    public VariableImpl(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public T eval(EvalContext context) {
        return context.var(name());
    }
}
