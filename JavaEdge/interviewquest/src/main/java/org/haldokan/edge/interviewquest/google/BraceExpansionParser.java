package org.haldokan.edge.interviewquest.google;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Write code that would parse an expression that is similar to BASH brace expansion. Best illustrated with an example:
 * the expression "(a,b,cy)n,m" would be parsed into an array of the following strings:
 * an
 * bn
 * cyn
 * m
 * <p>
 * You can assume that the input will always be valid.
 * <p>
 * Hint: the expression can nest. Therefore, "((a,b)o(m,n)p,b)" parses into:
 * aomp
 * aonp
 * bomp
 * bonp
 * b
 * Created by haytham.aldokanji on 10/5/15.
 */
public class BraceExpansionParser {
    private Deque<Character> stack = new ArrayDeque<>();

    public static void main(String[] args) {
        BraceExpansionParser driver = new BraceExpansionParser();
        driver.parse("((a,b)o(m,n)p,b)");
    }

    public void parse(String expr) {
        for (int i = 0; i < expr.length(); i++) {
            char curr = expr.charAt(i);
            Character prev = stack.peek();
            if (exprOperand(curr) && prev != null && prev.charValue() == ')') {

            } else {
                stack.push(curr);
            }
        }
        System.out.println(stack);
    }

    private boolean exprOperand(char ch) {
        return !(ch == ')' || ch == '(' || ch == ',');
    }
}
