package org.haldokan.edge.interviewquest.amazon;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * My solution to an Amazon interview question
 * The Question: 3_STAR
 * Given a postfix expression as a string, evaluate it and return the result.
 * Example : "423+*" ->20. The Postfix expression is well formed(need not check for bad expression)
 */
public class PostfixExpressions {
    private static final String M = "*", A = "+", S = "-", D = "/";

    public static void main(String[] args) {
        System.out.println(eval("32423+*/-")); //should be 7: (2 + 3) * 4 / 2 - 3
        System.out.println(eval("423+*")); //should be 20
    }

    private static Double eval(String expr) {
        Deque<String> stack = new ArrayDeque<>();
        for (int i = 0; i < expr.length(); i++) {
            String chr = String.valueOf(expr.charAt(i));
            if (isOperator(chr)) {
                stack.push(apply(stack.pop(), stack.pop(), chr));
            } else {
                stack.push(chr);
            }
        }
        return Double.valueOf(stack.pop());
    }

    private static String apply(String chr1, String chr2, String operator) {
        if (operator.equals(M)) {
            return String.valueOf(Double.valueOf(chr1) * Double.valueOf(chr2));
        }
        if (operator.equals(A)) {
            return String.valueOf(Double.valueOf(chr1) + Double.valueOf(chr2));
        }
        if (operator.equals(S)) {
            return String.valueOf(Double.valueOf(chr1) - Double.valueOf(chr2));
        }
        if (operator.equals(D)) {
            return String.valueOf(Double.valueOf(chr1) / Double.valueOf(chr2));
        }
        throw new IllegalArgumentException("Invalid operator: " + operator);
    }

    private static boolean isOperator(String chr) {
        return chr != null && (chr.equals(M) || chr.equals(A) || chr.equals(S) || chr.equals(D));
    }
}
