package org.haldokan.edge.interviewquest.linkedin;

import java.util.Deque;
import java.util.LinkedList;

/**
 * My implementation to a Linkedin interview question found on Careercup.
 * The Question: 4_STAR
 * Compute the value of an expression in Reverse Polish Notation. Supported operators are "+", "-", "*" and "/". Reverse
 * Polish is a postfix mathematical notation in which each operator immediately follows its operands. Each operand may
 * be a number or another expression.
 * <p>
 * For example, 3 + 4 in Reverse Polish is 3 4 + and 2 * (4 + 1) would be written as 4 1 + 2 * or 2 4 1 + *
 *
 * param ops a sequence of numbers and operators, in Reverse Polish Notation
 * return the result of the computation
 * throws IllegalArgumentException
 * ops don't represent a well-formed RPN expression
 * throws ArithmeticException
 * the computation generates an arithmetic error, such as dividing by zero
 * <p>
 * Some sample ops and their results:
 * <p>
 * ["4", "1", "+", "2.5", "*"] -> ((4 + 1) * 2.5) -> 12.5
 * <p>
 * ["5", "80", "40", "/", "+"] -> (5 + (80 / 40)) -> 7
 */
public class ExpressionsInPolishRepr {
    public static void main(String[] args) {
        ExpressionsInPolishRepr exprCalc = new ExpressionsInPolishRepr();

        String[] expr = new String[]{"4", "1", "+", "2.5", "*"};
        System.out.println(exprCalc.exprVal(expr));

        expr = new String[]{"5", "80", "40", "/", "+"};
        System.out.println(exprCalc.exprVal(expr));
    }

    private double exprVal(String[] expression) {
        Deque<String> deck = new LinkedList<>();
        for (String part : expression) {
            if (isOperator(part)) {
                String opn2 = deck.pop();
                String opn1 = deck.pop();
                deck.addFirst(compute(opn1, part, opn2));
            } else {
                deck.addFirst(part);
            }
        }
        return Double.valueOf(deck.pop());
    }

    private boolean isOperator(String s) {
        return s.length() == 1 && "*/+-".contains(s);
    }

    private String compute(String opn1, String op, String opn2) {
        if (opn1 == null || opn2 == null)
            throw new IllegalArgumentException("Malformed expression: null operands");
        double val = 0;
        if (op.equals("+"))
            val = Double.valueOf(opn1) + Double.valueOf(opn2);
        if (op.equals("-"))
            val = Double.valueOf(opn1) - Double.valueOf(opn2);
        if (op.equals("*"))
            val = Double.valueOf(opn1) * Double.valueOf(opn2);
        if (op.equals("/")) {
            if (opn2.equals("0"))
                throw new ArithmeticException("Division by zero " + opn1 + "/" + opn2);
            val = Double.valueOf(opn1) / Double.valueOf(opn2);
        }
        return String.valueOf(val);
    }
}
