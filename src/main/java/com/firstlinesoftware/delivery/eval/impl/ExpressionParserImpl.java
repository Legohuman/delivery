package com.firstlinesoftware.delivery.eval.impl;

import com.firstlinesoftware.delivery.eval.api.Expression;
import com.firstlinesoftware.delivery.eval.api.ExpressionParser;
import com.firstlinesoftware.delivery.eval.api.FunctionAlias;
import com.firstlinesoftware.delivery.eval.api.Variable;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class ExpressionParserImpl implements ExpressionParser {

    @Override
    public Expression parse(String str) {
        String trimmed = StringUtils.trimToNull(str);
        if (trimmed != null) {
            int leftBraceIndex = trimmed.indexOf('(');

            if (leftBraceIndex != -1) {
                String fnName = StringUtils.trimToNull(trimmed.substring(0, leftBraceIndex));
                if (fnName == null) {
                    throw new IllegalArgumentException(String.format("Function name in expression %s can not be empty", str));
                }
                if (!trimmed.endsWith(")")) {
                    throw new IllegalArgumentException(String.format("Function invocation in expression %s should end with )", str));
                }
                String argListStr = trimmed.substring(leftBraceIndex + 1, trimmed.length() - 1);
                String[] argStrs = StringUtils.trimToEmpty(argListStr).split("\\s*,\\s*");
                List<Expression> args = Arrays.stream(argStrs).map(this::parse).collect(Collectors.toList());
                return EvalFactory.fn(fnName, args.toArray(new Expression[args.size()]));
            } else if (trimmed.startsWith(FunctionAlias.prefix)) {
                //assume no brackets
                return new FunctionAliasImpl<>(trimmed.substring(2, trimmed.length()));
            } else if (trimmed.startsWith(Variable.prefix)) {
                return new VariableImpl<>(trimmed.substring(1, trimmed.length()));
            } else if (trimmed.startsWith("'")) {
                if (!trimmed.endsWith("'")) {
                    throw new IllegalArgumentException(String.format("String literal in expression %s should end with '", str));
                }
                return new StringLiteral(trimmed.substring(1, trimmed.length() - 1));
            } else {
                if (validFirstDoubleLiteralChar(trimmed)) {
                    try {
                        return new DoubleLiteral(new Double(trimmed));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(String.format("Double literal in expression %s has invalid format", str));
                    }
                }else{
                    throw new IllegalArgumentException(String.format("Unable to determine type of expression %s", str));
                }
            }
        }
        return null;
    }

    private boolean validFirstDoubleLiteralChar(String str) {
        char c = str.charAt(0);
        return (c > '0' && c < '9') || c == '.' || c == '+' || c == '-';
    }
}
