package com.GunterPro7uDerKatzenLord.Utils;

import java.util.Stack;

public class MathUtils {
    public static double eval(String expression) {
        expression = expression.replaceAll(",", ".").replaceAll("\\s", ""); // Ersetze "x" durch "*"
        String[] tokens = expression.split("(?<=[-+*/()])|(?=[-+*/()])");
        Stack<Double> values = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }

            if (token.matches("\\d+(\\.\\d+)?")) {
                values.push(Double.parseDouble(token));
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    applyOperator(values, operators);
                }
                operators.pop(); // Pop the "("
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && hasPrecedence(token, operators.peek())) {
                    applyOperator(values, operators);
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            applyOperator(values, operators);
        }

        if (values.size() != 1 || !operators.isEmpty()) {
            throw new IllegalArgumentException("Invalid expression");
        }

        return values.pop();
    }

    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private static boolean hasPrecedence(String operator1, String operator2) {
        if ((operator1.equals("*") || operator1.equals("/")) && (operator2.equals("+") || operator2.equals("-"))) {
            return true;
        }
        return false;
    }

    private static void applyOperator(Stack<Double> values, Stack<String> operators) {
        if (values.size() < 2 || operators.isEmpty() || operators.peek().equals("(")) {
            throw new IllegalArgumentException("Invalid expression");
        }

        double operand2 = values.pop();
        double operand1 = values.pop();
        String operator = operators.pop();
        double result;

        switch (operator) {
            case "+":
                result = operand1 + operand2;
                break;
            case "-":
                result = operand1 - operand2;
                break;
            case "*":
                result = operand1 * operand2;
                break;
            case "/":
                if (operand2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = operand1 / operand2;
                break;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }

        values.push(result);
    }
}

