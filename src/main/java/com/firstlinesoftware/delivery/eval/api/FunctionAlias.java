package com.firstlinesoftware.delivery.eval.api;

import java.util.List;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public interface FunctionAlias<T> extends Expression<T> {

    String prefix = "f$";

    String name();
}
