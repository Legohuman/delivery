package com.firstlinesoftware.delivery.eval.impl;

import com.firstlinesoftware.delivery.eval.api.*;
import com.firstlinesoftware.delivery.eval.impl.fn.MaxFunction;
import com.firstlinesoftware.delivery.eval.impl.fn.MinFunction;
import com.firstlinesoftware.delivery.eval.impl.fn.MultiplyFunction;
import com.firstlinesoftware.delivery.eval.impl.fn.SumFunction;

import java.util.Arrays;

/**
 * User: Legohuman
 * Date: 04/03/16
 */
public class EvalFactory {

    private enum FunctionEnum {
        sum {
            @Override
            Function<Double> create(Expression... args) {
                return new SumFunction(Arrays.asList(args));
            }
        },
        mul {
            @Override
            Function<Double> create(Expression... args) {
                return new MultiplyFunction(Arrays.asList(args));
            }
        },
        max {
            @Override
            Function<Double> create(Expression... args) {
                return new MaxFunction(Arrays.asList(args));
            }
        },
        min {
            @Override
            Function<Double> create(Expression... args) {
                return new MinFunction(Arrays.asList(args));
            }
        };

        abstract Function create(Expression... args);
    }

    @SuppressWarnings("unchecked")
    public static Function<Double> sum(Expression... args) {
        return FunctionEnum.sum.create(args);
    }

    @SuppressWarnings("unchecked")
    public static Function<Double> mul(Expression... args) {
        return FunctionEnum.mul.create(args);
    }

    @SuppressWarnings("unchecked")
    public static Function<Double> max(Expression... args) {
        return FunctionEnum.max.create(args);
    }

    @SuppressWarnings("unchecked")
    public static Function<Double> min(Expression... args) {
        return FunctionEnum.min.create(args);
    }

    @SuppressWarnings("unchecked")
    public static Function fn(String name, Expression... args) {
        FunctionEnum fn = FunctionEnum.valueOf(name);
        if (fn == null) {
            throw new IllegalArgumentException(String.format("Function with name %s can not be found in functions enum", name));
        }
        return fn.create(args);
    }

    public static Literal<String> val(String val) {
        return new StringLiteral(val);
    }

    public static Literal<Integer> val(int val) {
        return new IntLiteral(val);
    }

    public static Literal<Double> val(double val) {
        return new DoubleLiteral(val);
    }

    public static <T> Variable<T> var(String name) {
        return new VariableImpl<>(name);
    }

    public static <T> Variable<T> var(EvalContext context, String name, T val) {
        context.var(name, val);
        return var(name);
    }

    public static <T> FunctionAlias<T> fnAlias(String name) {
        return new FunctionAliasImpl<>(name);
    }

    public static <T> FunctionAlias<T> fnAlias(EvalContext context, String name, Function<T> fn) {
        context.fn(name, fn);
        return new FunctionAliasImpl<>(name);
    }

    public static EvalContext context() {
        return new EvalContextImpl();
    }
}
