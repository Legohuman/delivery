package com.firstlinesoftware.delivery.eval.api;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public interface Variable<T> extends Expression<T> {

    String prefix = "$";

    String name();
}
