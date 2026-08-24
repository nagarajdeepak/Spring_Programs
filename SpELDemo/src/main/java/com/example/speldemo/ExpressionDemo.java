package com.example.speldemo;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class ExpressionDemo {
	public static void main(String[] args) {

        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression("10 + 20");

        Integer result = expression.getValue(Integer.class);

        System.out.println("Result = " + result);
    }
}
