package org.haldokan.edge.interviewquest.amazon;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to an Amazon interview question
 * The Question: 3_STAR
 * <p>
 * With input as a integer, write an algorithm to convert that to string without using any built in functions.
 * It is a signed number.
 * <p>
 * Equivalent to String.valueOf(-123); //java
 * Created by haytham.aldokanji on 7/6/16.
 */
public class ConvertIntegerToString {
    public static void main(String[] args) {
        assertThat(numToString(-12345), is("-12345"));
        assertThat(numToString(12345), is("12345"));
        assertThat(numToString(0), is("0"));
        assertThat(numToString(1), is("1"));
        assertThat(numToString(-1), is("-1"));
        assertThat(numToString(10), is("10"));
    }

    public static String numToString(int integer) {
        int number = integer;
        StringBuilder result = new StringBuilder();

        char sign = 0; // null char
        if (number < 0) {
            number *= -1;
            sign = '-';
        }
        if (integer == 0) {
            result.append(integer);
        }
        while (number > 0) {
            int rightMostDigit = number % 10;
            result.insert(0, rightMostDigit);
            number /= 10;
        }
        if (sign != 0) {
            result.insert(0, sign);
        }
        return result.toString();
    }
}
