package com.firstlinesoftware.delivery.eval.impl;

import com.firstlinesoftware.delivery.eval.api.EvalContext;
import com.firstlinesoftware.delivery.eval.api.Literal;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public class AbstractLiteral<T> implements Literal<T> {

    private final T val;

    public AbstractLiteral(T val) {
        this.val = val;
    }

    public T value() {
        return val;
    }

    public T eval(EvalContext context) {
        return value();
    }
}
