package com.firstlinesoftware.delivery.eval.api;

import java.util.List;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public interface Function<T> extends Expression<T> {

    List<Expression> arguments();

    Function<T> arguments(Expression... args);
}
