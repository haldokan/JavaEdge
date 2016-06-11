package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - I used a conventional solution with an evaluation stack. There must
 * be a way to solve this question using dynamic programming which perhaps give more optimal cost (i.e # modifications)
 * The Question: 4_STAR
 * Imagine x is an operand and * is a binary operator. We say a string of x and * follows Reverse Polish notation
 * if it is a postfix notation.
 * <p>
 * For example strings xx*, x, and xx*xx** follow Reverse Polish notation.
 * <p>
 * Given a string of x and *, how many insert, delete, and replace operations are needed to make the string follow the RPN.
 * <p>
 * For example, xx* need 0 operation to follow RPN since it already follows RPN.
 * x*x needs two operations to become xx* which follows RPN.
 * *xx* needs one operation to become xx* which follows RPN.
 * <p>
 * Your algorithm should work for a string of size up to 100.
 * <p>
 * Created by haytham.aldokanji on 5/8/16.
 */
public class ConvertExprToReversePolishNotation {
    private static final char BIN_OP = '*';

    public static void main(String[] args) {
        ConvertExprToReversePolishNotation driver = new ConvertExprToReversePolishNotation();
        driver.test();
    }

    public int reversePolishNotation(String input) {
        int numModifications = 0;
        char[] parts = input.toCharArray();
        Deque<Character> evalStack = new ArrayDeque<>();

        for (char part : parts) {
            if (isBinOp(part)) {
                if (evalStack.isEmpty()) {
                    numModifications++;
                } else if (isOperand(evalStack.peek())) {
                    numModifications += evalSubExpr(evalStack, part);
                } else if (isBinOp(evalStack.peek())) {
                    evalStack.pop();
                } else {
                    // cost of removing the operator
                    numModifications++;
                }
            } else {
                evalStack.push(part);
            }
        }
        numModifications += postEval(evalStack);
        return numModifications;
    }

    private int postEval(Deque<Character> evalStack) {
        int modifications = 0;
        // must be trailing x's w/o operator to trigger evaluation the sub expression
        if (evalStack.size() > 1) {
            // add 1 bcz we insert the operator
            modifications += 1 + evalSubExpr(evalStack, BIN_OP);
        }
        return modifications;
    }

    private int evalSubExpr(Deque<Character> evalStack, char operator) {
        Character entry = evalStack.pop();
        int count = 0;
        while (isOperand(entry)) {
            count++;
            entry = evalStack.peek();
            if (entry != null) {
                entry = evalStack.pop();
            }
        }
        int modifications = 0;
        if (count == 1) {
            // add 2 bcz we have to remove both operand and operator
            modifications += 2;
        } else if (count == 2) {
            evalStack.push(operator);
        } else if (count > 2) {
            if (count % 2 == 0) {
                modifications += count / 2 - 1;
            } else {
                modifications += count / 2;
            }
            evalStack.push(operator);
        }
        return modifications;
    }

    private boolean isBinOp(Character c) {
        return c != null && c == BIN_OP;
    }

    private boolean isOperand(Character c) {
        return c != null && !isBinOp(c);
    }

    private void test() {
        String input = "xx*";
        int cost = reversePolishNotation(input);
        assertThat(cost, is(0));

        input = "xx*xx**";
        cost = reversePolishNotation(input);
        assertThat(cost, is(0));

        input = "xxx*xx**";
        cost = reversePolishNotation(input);
        assertThat(cost, is(1));

        // this one is corrected to x (not xx*). Still the cost is the same
        input = "x*x";
        cost = reversePolishNotation(input);
        assertThat(cost, is(2));

        input = "*xx*";
        cost = reversePolishNotation(input);
        assertThat(cost, is(1));

        input = "x";
        cost = reversePolishNotation(input);
        assertThat(cost, is(0));

        input = "xxxxx";
        cost = reversePolishNotation(input);
        assertThat(cost, is(3));

        input = "xxxx";
        cost = reversePolishNotation(input);
        assertThat(cost, is(2));
    }
}
