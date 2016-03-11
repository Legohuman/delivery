package com.firstlinesoftware.delivery.eval.test;

import com.firstlinesoftware.delivery.eval.api.*;
import com.firstlinesoftware.delivery.eval.impl.ExpressionParserImpl;
import org.junit.Test;

import static com.firstlinesoftware.delivery.eval.impl.EvalFactory.*;
import static org.junit.Assert.assertEquals;

/**
 * User: Legohuman
 * Date: 04/03/16
 */

public class FunctionsTest {

    @Test
    public void testVariable() {
        EvalContext context = context();
        int result = var(context, "var", 10).eval(context);
        System.out.println(result);
        assertEquals(10, result);
    }

    @Test
    public void testSumFn() {
        Double result = sum(val(2.0), val(3.0)).eval(context());
        System.out.println(result);
        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSumFnWithVars() {
        EvalContext context = context();
        Variable<Double> var1 = var(context, "var1", 10d);
        Variable<Double> var2 = var(context, "var2", 15d);

        Double result = sum(var1, var2).eval(context);
        System.out.println(result);
        assertEquals(25d, result, 1e-6);
    }

    @Test
    public void testSumFnWithOtherFn() {
        EvalContext context = context();
        Variable<Double> var1 = var(context, "var1", 10d);
        Variable<Double> var2 = var(context, "var2", 15d);

        Double result = sum(var1, sum(var2, val(2d), val(3d))).eval(context);
        System.out.println(result);
        assertEquals(30d, result, 1e-6);
    }

    @Test
    public void testMulFn() {
        Double result = mul(val(2.0), val(3.0)).eval(context());
        System.out.println(result);
        assertEquals(6.0, result, 1e-6);
    }

    @Test
    public void testMaxFn() {
        Literal<Double> val1 = val(2.0);
        Literal<Double> val2 = val(3.0);
        Double result = max(mul(val1, val2), sum(val1, val2)).eval(context());
        System.out.println(result);
        assertEquals(6.0, result, 1e-6);
    }

    @Test
    public void testMinFn() {
        Literal<Double> val1 = val(2.0);
        Literal<Double> val2 = val(3.0);
        Double result = min(mul(val1, val2), sum(val1, val2)).eval(context());
        System.out.println(result);
        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSumFnDifferentContexts() {
        String v1 = "var1";
        String v2 = "var2";

        EvalContext context1 = context();
        var(context1, v1, 10d);
        var(context1, v2, 15d);

        EvalContext context2 = context();
        var(context2, v1, 2d);
        var(context2, v2, 4d);

        Function<Double> sum = sum(var(v1), var(v2));

        Double result1 = sum.eval(context1);
        System.out.println(result1);
        assertEquals(25d, result1, 1e-6);

        sum.arguments(var(v1), var(v2));
        Double result2 = sum.eval(context2);
        System.out.println(result2);
        assertEquals(6d, result2, 1e-6);
    }

    @Test
    public void testFnAlias() {
        EvalContext context = context();
        Literal<Double> val1 = val(2.0);
        Literal<Double> val2 = val(3.0);
        FunctionAlias<Double> mulAlias = fnAlias(context, "mul2", mul(val1, val2));

        Double result = mulAlias.eval(context);
        System.out.println(result);
        assertEquals(6.0, result, 1e-6);
    }

    @Test
    public void testParseExpression() {
        EvalContext context = context();
        var(context, "var1", 10d);

        Double result = (Double) new ExpressionParserImpl().parse("sum(5, $var1)").eval(context);
        System.out.println(result);
        assertEquals(15d, result, 1e-6);
    }

    @Test
    public void testParseExpressionWithFnAlias() {
        String v1 = "var1";
        String v2 = "var2";
        String mulFnName = "mul2";
        Function<Double> mul = mul(var(v1), var(v2));

        EvalContext context = context();
        context.fn(mulFnName, mul);
        var(context, v1, 10d);
        var(context, v2, 15d);

        EvalContext context2 = context();
        context2.fn(mulFnName, mul);
        var(context2, v1, 20d);
        var(context2, v2, 25d);

        ExpressionParserImpl parser = new ExpressionParserImpl();

        Double result = (Double) parser.parse("f$mul2").eval(context);
        System.out.println(result);
        assertEquals(150d, result, 1e-6);

        Double result2 = (Double) parser.parse("f$mul2").eval(context2);
        System.out.println(result2);
        //todo add FunctionInvocation obj and fix test
//        assertEquals(500d, result2, 1e-6);
    }

}
