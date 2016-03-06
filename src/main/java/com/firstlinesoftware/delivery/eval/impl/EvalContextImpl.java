package com.firstlinesoftware.delivery.eval.impl;

import com.firstlinesoftware.delivery.eval.api.EvalContext;
import com.firstlinesoftware.delivery.eval.api.Function;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public class EvalContextImpl implements EvalContext {

    private final Map<String, Function> functions = new HashMap<>();
    private final Map<String, Object> vars = new HashMap<>();

    public EvalContextImpl() {
    }

    @SuppressWarnings("unchecked")
    public <T> Function<T> fn(String key) {
        return (Function<T>) functions.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> Function<T> fn(String key, Function<T> var) {
        return (Function<T>) functions.put(key, var);
    }

    @SuppressWarnings("unchecked")
    public <T> T var(String key) {
        return (T) vars.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T var(String key, T var) {
        return (T) vars.put(key, var);
    }
}
