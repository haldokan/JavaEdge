package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * @InProgress Implement atof function. eg., +3.5e-2, .03e1, 1e1, 0.0
 * Created by haytham.aldokanji on 5/4/16.
 */
public class CLanguageAtof {
    private static final char[] DIGITS = "0123456789".toCharArray();

    public static void main(String[] args) {
        CLanguageAtof driver = new CLanguageAtof();

        String ascii = "+3.5e-2";
        float number = driver.atof(ascii);
        System.out.println(number);

        ascii = ".03e1";
        number = driver.atof(ascii);
        System.out.println(number);

        ascii = "1e1";
        number = driver.atof(ascii);
        System.out.println(number);

        ascii = "0.0";
        number = driver.atof(ascii);
        System.out.println(number);
    }

    public float atof(String ascii) {
        int integralPart = 0;
        float decimalPart = 0f;
        float exponentPart = 1f;

        int numberSign = 1;
        int exponentSign = 1;

        char[] parts = ascii.toCharArray();
        Deque<Character> evalDeck = new ArrayDeque<>();
        Context context = Context.INTEGRAL;

        for (char part : parts) {
            if (isSign(part)) {
                int[] multipliers = multiplierSigns(evalDeck, part);
                numberSign *= multipliers[0];
                exponentSign *= multipliers[1];
            } else if (isPower(part)) {
                if (context == Context.INTEGRAL) {
                    integralPart = calculateIntegral(evalDeck, numberSign);
                } else if (context == Context.DECIMAL) {
                    decimalPart = calculateDecimal(evalDeck);
                }
                context = Context.EXPONENT;
            } else if (isDecimalPoint(part)) {
                integralPart = calculateIntegral(evalDeck, numberSign);
                context = Context.DECIMAL;
            } else if (isDigit(part)) {
                if (context == Context.INTEGRAL || context == Context.EXPONENT) {
                    evalDeck.push(part);
                } else {
                    evalDeck.addFirst(part);
                }
            } else {
                throw new IllegalArgumentException("ASCII string is malformatted");
            }
        }
        if (context == Context.INTEGRAL) {
            integralPart = calculateIntegral(evalDeck, numberSign);
        } else if (context == Context.EXPONENT) {
            exponentPart = calculateExponent(evalDeck, exponentSign);
        }

        return (integralPart + decimalPart) * exponentPart;
    }

    private int[] multiplierSigns(Deque<Character> evalDeck, char part) {
        int signMultiplier = getSignMultiplier(part);
        Character val = evalDeck.peek();

        int[] multipliers = new int[]{1, 1};
        if (val == null) {
            multipliers[0] = signMultiplier;
        } else if (isPower(part)) {
            multipliers[1] = signMultiplier;
        } else {
            throw new IllegalArgumentException("ASCII string is malformatted");
        }
        return multipliers;
    }

    private float calculateExponent(Deque<Character> evalDeck, int exponentSign) {
        // let's not use Math.pow
        int exponent = calculateIntegral(evalDeck, 1);
        float tenToExponent = 1;
        for (int i = 0; i < exponent; i++) {
            tenToExponent *= 10;
        }
        if (exponentSign == -1) {
            tenToExponent = 1 / tenToExponent;
        }
        return tenToExponent;
    }

    private int calculateIntegral(Deque<Character> evalDeck, int numberSign) {
        int integral = 0;
        int multiplier = 1;

        while (!evalDeck.isEmpty()) {
            integral += fromAsciiToDigit(evalDeck.pop()) * multiplier;
            multiplier *= 10;
        }
        return numberSign * integral;
    }

    private float calculateDecimal(Deque<Character> evalDeck) {
        float decimal = 0f;
        int divisor = 10;

        while (!evalDeck.isEmpty()) {
            decimal += fromAsciiToDigit(evalDeck.removeFirst()) / divisor;
            divisor /= 10;
        }
        return decimal;
    }

    private boolean isSign(char part) {
        return part == '+' || part == '-';
    }

    private boolean isMinus(char part) {
        return part == '-';
    }

    private int getSignMultiplier(char part) {
        if (isMinus(part)) {
            return -1;
        }
        return 1;
    }

    private boolean isPower(char part) {
        return part == 'e' || part == 'E';
    }

    private boolean isDecimalPoint(char part) {
        return part == '.';
    }

    private boolean isDigit(char part) {
        // let's not use Character.isDigit
        return Arrays.binarySearch(DIGITS, part) >= 0;
    }

    private int fromAsciiToDigit(char part) {
        return Arrays.binarySearch(DIGITS, part);
    }

    private enum Context {INTEGRAL, DECIMAL, EXPONENT}

}

