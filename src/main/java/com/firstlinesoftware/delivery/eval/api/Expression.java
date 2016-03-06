package com.firstlinesoftware.delivery.eval.api;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public interface Expression<T> {

    T eval(EvalContext context);
}
