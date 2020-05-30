package org.haldokan.edge.interviewquest.facebook;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - I provide another solution SumBinaryStrings.java that solves the
 * problem in a more conventional way using array of chars
 * The Question: 4_STAR
 * Code a function that gets two strings representing binary numbers (so the only possible characters are '1' and '0',
 * and returns a third string representing the sum of the input.
 * The input strings don't necessarily have of the same length.
 * <p>
 * Tell the complexity of the solution.
 * <p>
 * Created by haytham.aldokanji on 5/6/16.
 */
public class SumBinaryStringsBitwise {

    public static void main(String[] args) {
        SumBinaryStringsBitwise driver = new SumBinaryStringsBitwise();
        driver.testFromBinStringToLong();
        driver.testFromLongToBinString();
        driver.testBinStringAdd();
    }

    public String binStringAdd(String s1, String s2) {
        long sum = fromBinStringToLong(s1) + fromBinStringToLong(s2);
        return fromLongToBinString(sum);
    }

    private long fromBinStringToLong(String binString) {
        long number = 0L;

        for (int i = binString.length() - 1; i >= 0; i--) {
            if (binString.charAt(i) == '1') {
                number |= 1 << (binString.length() - i - 1);
            }
        }
        return number;
    }

    private String fromLongToBinString(long number) {
        boolean[] bits = new boolean[64];
        int index = 0;
        long copy = number;
        while (copy != 0) {
            copy = number >> index;
            //stack'em
            bits[bits.length - index - 1] = (copy & 1) == 1;
            index++;
        }

        StringBuilder sb = new StringBuilder(bits.length);
        int indexHighOrderZero = 0;

        for (int i = 0; i < bits.length; i++) {
            boolean bit = bits[i];
            // get rid of high-order 0's
            if (indexHighOrderZero == i && !bit) {
                indexHighOrderZero++;
            } else {
                sb.append(bit ? '1' : '0');
            }
        }
        return sb.toString();
    }


    private void testBinStringAdd() {
        long num1 = 798798343L;
        long num2 = 907937L;
        long sum = num1 + num2;
        assertThat(binStringAdd(Long.toBinaryString(num1), Long.toBinaryString(num2)),
                is(Long.toBinaryString(sum)));

        num1 = 1111111111L;
        num2 = 11111L;
        sum = num1 + num2;
        assertThat(binStringAdd(Long.toBinaryString(num1), Long.toBinaryString(num2)),
                is(Long.toBinaryString(sum)));
    }

    private void testFromBinStringToLong() {
        long expected = 93873290L;
        String binString = Long.toBinaryString(expected);

        long number = fromBinStringToLong(binString);
        assertThat(number, is(expected));
    }

    private void testFromLongToBinString() {
        long number = 34785943L;
        String expected = Long.toBinaryString(number);

        String binString = fromLongToBinString(number);
        assertThat(binString, is(expected));
    }
}
