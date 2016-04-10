package org.haldokan.edge.interviewquest.amazon;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * My solution to an Amazon interview question
 * <p>
 * Given a postfix expression as a string, evaluate it and return the result.
 * Example : "423+*" ->20. The Postfix expression is well formed(need not check for bad expression)
 */
public class PostfixExpressions {
    private static final String M = "*", A = "+", S = "-", D = "/";

    public static void main(String[] args) {
        System.out.println(eval("32423+*/-")); //should be 7: (2 + 3) * 4 / 2 - 3
    }

    private static Double eval(String expr) {
        Deque<String> stack = new ArrayDeque<>();

        for (int i = expr.length() - 1; i >= 0; i--) {
            String chr = String.valueOf(expr.charAt(i));
            if (isOperator(chr)) {
                stack.push(chr);
            } else {
                String topChr = stack.peek();
                if (!isOperator(topChr)) {
                    stack.pop();
                    String operator = stack.pop();
                    String val = apply(chr, topChr, operator);
                    stack.push(val);
//                    System.out.println("pushed: " + val);
                } else {
                    stack.push(chr);
                }
            }
        }
        return Double.valueOf(stack.pop());
    }

    private static String apply(String chr, String topChr, String operator) {
        if (operator.equals(M)) {
            return String.valueOf(Double.valueOf(chr) * Double.valueOf(topChr));
        }
        if (operator.equals(A)) {
            return String.valueOf(Double.valueOf(chr) + Double.valueOf(topChr));
        }
        if (operator.equals(S)) {
            return String.valueOf(Double.valueOf(topChr) - Double.valueOf(chr));
        }
        if (operator.equals(D)) {
            return String.valueOf(Double.valueOf(topChr) / Double.valueOf(chr));
        }
        throw new IllegalArgumentException("Invalid operator: " + operator);
    }

    private static boolean isOperator(String chr) {
        return chr.equals(M) || chr.equals(A) || chr.equals(S) || chr.equals(D);
    }
}
