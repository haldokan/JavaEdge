package org.haldokan.edge.interviewquest.facebook;

import java.util.*;
import java.util.function.BiFunction;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Facebook interview question - parses linear equations and resolve the value of the variable
 * The Question: 5_STAR
 * Given an expression (in a single variable) like 4x+13(x-(4x+x/3)) = 9, evaluate x
 * The expression is a string and the variable is always x.
 * <p>
 * Created by haytham.aldokanji on 4/30/16.
 */
public class EvaluateVarInLinearEquation {

    public static void main(String[] args) {
        EvaluateVarInLinearEquation driver = new EvaluateVarInLinearEquation();

        String expression1 = "x + (x + 2x) - 3 = 9";
        Double value1 = driver.eval(expression1);
        System.out.println(expression1 + " => " + value1);
        assertThat(value1, is(3.0));

        String expression2 = "420 + 36(10x - 120)/12 + 12 = 10";
        Double value2 = driver.eval(expression2);
        System.out.println(expression2 + " => " + value2);
        assertThat(value2, greaterThan(-2.06667));
        assertThat(value2, lessThan(-2.06666));

        String expression3 = "4x + 13(x - (4x + x/3)) = 9";
        Double value3 = driver.eval(expression3);
        System.out.println(expression3 + " => " + value3);
        assertThat(value3, greaterThan(-0.22882));
        assertThat(value3, lessThan(-0.22881));

        String expression4 = "(((x + 2))) = 4";
        Double value4 = driver.eval(expression4);
        System.out.println(expression4 + " => " + value4);
        assertThat(value4, is(2.0));

        String expression5 = "(((x + 2))) + 4 = 4";
        Double value5 = driver.eval(expression5);
        System.out.println(expression5 + " => " + value5);
        assertThat(value5, is(-2.0));

        String expression6 = "(x + 2)/2 + 4 = 4";
        Double value6 = driver.eval(expression6);
        System.out.println(expression6 + " => " + value6);
        assertThat(value6, is(-2.0));

        String expression7 = "4(x + 2)/2 + 8 = 4";
        Double value7 = driver.eval(expression7);
        System.out.println(expression7 + " => " + value7);
        assertThat(value7, is(-4.0));
    }

    public double eval(String expr) {
        String[] equationParts = expr.split("=");
        Operand expressionRightPart = Operand.create(equationParts[1].trim());

        String[] exprParts = Arrays.stream(equationParts[0].trim().split(""))
                .filter(s -> !s.equals(" "))
                .toArray(String[]::new);
        exprParts = combineDigits(exprParts);

        Deque<String> expressionQueue = new ArrayDeque<>();
        expressionQueue.addAll(Arrays.asList(exprParts));

        Deque<String> evalStack = new ArrayDeque<>();

        while (!expressionQueue.isEmpty()) {
            String part = expressionQueue.removeFirst();

            if (Operand.isNumber(part)) {
                evalNum(evalStack, part);
            } else if (Operand.isVar(part)) {
                evalVar(evalStack, part);
            } else if (ExpressionUtil.isEndSubExpr(part)) {
                String operatorB4StartExpr = evalStack.peek();
                if (operatorB4StartExpr != null && ExpressionUtil.isEndSubExpr(operatorB4StartExpr)) {
                    insureExprEvaluation(expressionQueue, part);
                } else {
                    evalSubExpression(evalStack);
                }
            } else if (ExpressionUtil.isStartSubExpr(part)) {
                String operatorB4StartExpr = evalStack.peek();
                if (operatorB4StartExpr != null && ExpressionUtil.isSubtraction(operatorB4StartExpr)) {
                    insureExprNegation(evalStack);
                }
                evalStack.push(part);
            } else if (ExpressionUtil.isAddOrSubtract(part)) {
                String operatorB4StartExpr = evalStack.peek();
                if (operatorB4StartExpr != null && ExpressionUtil.isEndSubExpr(operatorB4StartExpr)) {
                    insureExprEvaluation(expressionQueue, part);
                } else {
                    evalStack.push(part);
                }
            } else {
                evalStack.push(part);
            }
        }
        return solve(evalStack, expressionRightPart);
    }

    private void insureExprEvaluation(Deque<String> expressionQueue, String part) {
        expressionQueue.addFirst(part);
        expressionQueue.addFirst("1");
        expressionQueue.addFirst("/");
    }

    private void insureExprNegation(Deque<String> evalStack) {
        evalStack.pop();
        evalStack.push("+");
        evalStack.push("-1");
    }

    private String[] combineDigits(String[] expr) {
        List<String> exprParts = new ArrayList<>();
        StringBuilder number = new StringBuilder();

        for (String part : expr) {
            if (Operand.isNumber(part)) {
                number.append(part);
            } else {
                if (number.length() > 0) {
                    exprParts.add(number.toString());
                    number.delete(0, number.length());
                }
                exprParts.add(part);
            }
        }
        if (number.length() > 0) {
            exprParts.add(number.toString());
        }
        return exprParts.toArray(new String[exprParts.size()]);
    }

    private Double solve(Deque<String> evalStack, Operand expressionRightPart) {
        evalSubExpression(evalStack, true); // should be of the form (x + 3) or or (x - 3) or 4x
        evalStack.pop(); // closing bracket
        Operand operand1 = Operand.create(evalStack.pop());
        String operator = evalStack.peek();

        Optional<Operand> operand2 = Optional.empty();
        if (operator != null && !ExpressionUtil.isStartSubExpr(operator)) {
            evalStack.pop();
            operand2 = Optional.of(Operand.create(evalStack.pop()));
            if (ExpressionUtil.isSubtraction(operator)) {
                operand1 = operand1.negate();
            }
        }
        evalStack.pop(); //closing bracket

        if (!operand1.isVar && (!operand2.isPresent() || !operand2.get().isVar)) {
            throw new IllegalArgumentException("Expression has no variable: " + operand1 + "\n" + operand2);
        }

        Operand rightPart = expressionRightPart;
        Operand leftPart = operand1;

        if (!operand1.isVar) {
            rightPart = rightPart.subtract(operand1);
            leftPart = operand2.get();
        } else if (operand2.isPresent()) {
            rightPart = rightPart.subtract(operand2.get());
        }

        return rightPart.getValue() / leftPart.getValue();
    }


    private void evalSubExpression(Deque<String> evalStack) {
        evalSubExpression(evalStack, false);
    }

    private void evalSubExpression(Deque<String> evalStack, boolean normalized) {
        List<Operand> parts = new ArrayList<>();

        while (!evalStack.isEmpty()) {
            String part = evalStack.pop();
            if (normalized && ExpressionUtil.isStartOrEndExpression(part)) {
                continue;
            }
            if (ExpressionUtil.isStartSubExpr(part)) {
                break;
            }
            if (Operand.isNumber(part) || Operand.isVar(part)) {
                Operand operand = Operand.create(part);

                String sign = evalStack.peek();
                if (sign != null) {
                    if (ExpressionUtil.isSubtraction(sign)) {
                        operand = operand.negate();
                        evalStack.pop();
                    } else if (ExpressionUtil.isAddition(sign)) {
                        evalStack.pop();
                    }
                }
                parts.add(operand);
            }
        }
        Optional<Operand> var = parts.stream().filter(Operand::isVar).reduce(Operand::add);
        Optional<Operand> number = parts.stream().filter(o -> !o.isVar).reduce(Operand::add);

        String partB4Expr = evalStack.peek();

        if (Operand.isNumber(partB4Expr)) {
            evalStack.pop();
            Operand operandB4Expr = Operand.create(partB4Expr);
            if (var.isPresent()) {
                var = Optional.of(var.get().multiply(operandB4Expr));
            }
            if (number.isPresent()) {
                number = Optional.of(number.get().multiply(operandB4Expr));
            }
        }
        evalStack.push("(");
        var.ifPresent(operand -> evalStack.push(operand.getStringValue()));
        if (number.isPresent()) {
            if (var.isPresent()) {
                // we push + since we negated the numbers when the sign was -
                evalStack.push("+");
            }
            evalStack.push(number.get().getStringValue());
        }

        evalStack.push(")");
    }

    private void evalNum(Deque<String> evalStack, String number) {
        String previousPart = evalStack.peek();

        if (Operand.isVar(previousPart)) {
            evalStack.pop();
            evalStack.push(Operand.create(previousPart).multiply(Operand.create(number)).getStringValue());
        } else if (ExpressionUtil.isDivision(previousPart)) {
            evalStack.pop();
            Operand denominator = Operand.create(number);
            String candidateNumenator = evalStack.pop();

            if (Operand.isNumber(candidateNumenator) || Operand.isVar(candidateNumenator)) {
                Operand numerator = Operand.create(candidateNumenator);
                evalStack.push(numerator.divide(denominator).getStringValue());
            } else if (ExpressionUtil.isEndSubExpr(candidateNumenator)) {
                applyFuncToSubExpression(evalStack, number, Operand::divide);
            }
        } else {
            evalStack.push(number);
        }
    }

    private void applyFuncToSubExpression(Deque<String> evalStack, String number, BiFunction<Operand, Operand, Operand> function) {
        //the sub-expression will have been already reduced to 2 terms - examples: (2x + 3) or (x) or (3)
        Operand operand1 = Operand.create(evalStack.pop());
        Optional<Operand> operand2 = Optional.empty();

        String operator = evalStack.pop(); // could be the closing bracket - ex: (3x)/4
        if (!ExpressionUtil.isStartSubExpr(operator)) {
            operand2 = Optional.of(Operand.create(evalStack.pop()));
            evalStack.pop(); // opening bracket
        }

        Operand operand3 = Operand.create(number);
        Operand result = function.apply(operand1, operand3);
        evalStack.push(result.getStringValue());
        if (operand2.isPresent()) {
            evalStack.push(operator);
            Operand result2 = function.apply(operand2.get(), operand3);
            evalStack.push(result2.getStringValue());
        }
    }


    private void evalVar(Deque<String> evalStack, String var) {
        String previousPart = evalStack.peek();
        if (Operand.isNumber(previousPart)) {
            Operand mult = Operand.create(previousPart).multiply(Operand.create(var));
            evalStack.pop();
            evalStack.push(mult.getStringValue());
        } else {
            evalStack.push(var);
        }
    }

    private static class Operand {
        private final Double value;
        private final String stringValue;
        private final String var;
        private final boolean isVar;

        public Operand(String value) {
            this.stringValue = value;
            this.var = (value.indexOf('x') > -1) ? "x" : "";

            if (Operand.isVar(var)) {
                isVar = true;
                if (value.length() == 1) {
                    this.value = 1d;
                } else {
                    this.value = Double.valueOf(value.substring(0, value.length() - 1));
                }
            } else {
                this.value = Double.valueOf(value);
                isVar = false;
            }
        }

        public static Operand create(String stringValue) {
            return new Operand(stringValue);
        }

        public static boolean isNumber(String s) {
            if (s == null) {
                return false;
            }
            String copy = s;
            char first = copy.charAt(0);
            if (first == '+' || first == '-') {
                if (copy.length() == 1) {
                    return false;
                }
                copy = copy.substring(1, copy.length());
            }
            for (char c : copy.toCharArray()) {
                // account for decimal points
                if (!(Character.isDigit(c) || c == '.')) {
                    return false;
                }
            }
            return true;
        }

        public static boolean isVar(String s) {
            if (s == null) {
                return false;
            }
            if (s.equals("x")) {
                return true;
            }
            if (s.length() > 1) {
                String part1 = s.substring(0, s.length() - 1);
                char part2 = s.charAt(s.length() - 1);
                return isNumber(part1) && part2 == 'x';
            }
            return false;
        }

        public boolean isVar() {
            return isVar;
        }

        public String getStringValue() {
            return stringValue;
        }

        public Double getValue() {
            return value;
        }

        public Operand multiply(Operand other) {
            if (isVar(stringValue) && isVar(other.getStringValue())) {
                throw new IllegalArgumentException("Both multiplication operands are vars: " + this + "\n" + other);
            }
            return new Operand((value * other.getValue()) + var + other.var);
        }

        public Operand divide(Operand denominator) {
            if (isVar(denominator.getStringValue())) {
                throw new IllegalArgumentException("Malformatted expression - var in denominator: " + denominator);
            }
            return new Operand((value / denominator.getValue()) + var);
        }

        public Operand add(Operand other) {
            return operate(other, Double::sum);
        }

        public Operand subtract(Operand other) {
            return operate(other, (v1, v2) -> v1 - v2);
        }

        public Operand operate(Operand other, BiFunction<Double, Double, Double> operation) {
            if (isVar && other.isVar || !(isVar || other.isVar)) {
                return new Operand((operation.apply(value, other.getValue())) + var);
            }
            throw new IllegalStateException("Can only add similar operands");
        }

        public Operand negate() {
            return new Operand((-1 * value) + var);
        }

        @Override
        public String toString() {
            return "Operand{" +
                    "value=" + value +
                    ", stringValue='" + stringValue + '\'' +
                    ", var='" + var + '\'' +
                    ", isVar=" + isVar +
                    '}';
        }
    }

    private static class ExpressionUtil {

        public static boolean isDivision(String s) {
            return s != null && s.equals("/");
        }

        public static boolean isAddition(String s) {
            return s != null && s.equals("+");
        }

        public static boolean isSubtraction(String s) {
            return s != null && s.equals("-");
        }

        public static boolean isAddOrSubtract(String s) {
            return isAddition(s) || isSubtraction(s);
        }

        public static boolean isStartSubExpr(String s) {
            return s != null && s.equals("(");
        }

        public static boolean isEndSubExpr(String s) {
            return s != null && s.equals(")");
        }

        public static boolean isStartOrEndExpression(String part) {
            return isStartSubExpr(part) || isEndSubExpr(part);
        }
    }
}
