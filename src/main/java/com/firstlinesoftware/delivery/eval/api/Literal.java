package com.firstlinesoftware.delivery.eval.api;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public interface Literal<T> extends Expression<T> {

    T value();

    Literal<Void> undefined = new Literal<Void>() {
        public Void value() {
            throw new UnsupportedOperationException("Undefined literal has no value");
        }

        public Void eval(EvalContext context) {
            throw new UnsupportedOperationException("Undefined literal can not be evaluated");
        }
    };

    default boolean isUndefined(){
        return !isDefined();
    }

    default boolean isDefined(){
        return this != undefined;
    }
}
