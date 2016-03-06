package com.firstlinesoftware.delivery.eval.api;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public interface EvalContext {

    <T> Function<T> fn(String key);

    <T> Function<T> fn(String key, Function<T> fn);

    <T> T var(String key);

    <T> T var(String key, T val);
}
