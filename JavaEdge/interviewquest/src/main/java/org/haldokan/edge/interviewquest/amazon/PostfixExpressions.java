package org.haldokan.edge.interviewquest.amazon;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * My solution to an Amazon interview question
 * The Question: 3_STAR
 * Given a postfix expression as a string, evaluate it and return the result.
 * Example : "423+*" ->20. The Postfix expression is well-formed (need not check for bad expression)
 * Touched on 06/08/25 (partially suggested by AI)
 */
public class PostfixExpressions {
    public static void main(String[] args) {
        System.out.println(eval("32423+*/-")); // Should be 7: (2 + 3) * 4 / 2 - 3
        System.out.println(eval("423+*")); // Should be 20
        System.out.println(eval("595*+")); // Should be 9 * 5 + 5 = 50
    }

    private static int eval(String expr) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (char chr : expr.toCharArray()) {
            int num = chr - '0'; // Convert char to int
            if (Character.isDigit(chr)) {
                stack.push(num);
            } else {
                stack.push(apply(stack.pop(), stack.pop(), chr));
            }
        }
        return stack.pop();
    }

    private static int apply(int num1, int num2, int operator) {
        return switch (operator) {
            case '+' -> num1 + num2;
            case '-' -> num1 - num2;
            case '*' -> num1 * num2;
            case '/' -> num1 / num2;
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };
    }
}
