package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question
 * <p>
 * Code a function that gets two strings representing binary numbers (so the only possible characters are '1' and '0',
 * and returns a third string representing the sum of the input.
 * The input strings don't necessarily have of the same length.
 * <p>
 * Tell the complexity of the solution.
 * <p>
 * Created by haytham.aldokanji on 5/6/16.
 */
public class SumBinaryStrings {
    private static final String BIN_NUM = "01";
    private static final char[] BIN_ARR = new char[]{'0', '1'};

    public static void main(String[] args) {
        SumBinaryStrings driver = new SumBinaryStrings();
        driver.testBitAdd();
        driver.testBinStringAdd();
    }

    public String binStringAdd(String s1, String s2) {
        String[] strings = new String[]{s1, s2};
        Arrays.sort(strings, (v1, v2) -> v1.length() - v2.length());

        String str1 = strings[0];
        String str2 = strings[1];

        StringBuilder sb = new StringBuilder(str2);
        int carry = 0;
        int k = str1.length() - 1;

        for (int i = str2.length() - 1; i >= 0; i--) {
            if (k >= 0) {
                int[] sum = bitAdd(str2.charAt(i), str1.charAt(k), carry);
                sb.setCharAt(i, BIN_ARR[sum[0]]);
                carry = sum[1];
                k--;
            } else if (carry == 0) {
                break;
            } else {
                int[] sum = bitAdd(str2.charAt(i), '0', carry);
                sb.setCharAt(i, BIN_ARR[sum[0]]);
                carry = sum[1];
            }
        }
        if (carry == 1) {
            sb.insert(0, carry);
        }

        return sb.toString();
    }

    private int[] bitAdd(char bit1, char bit2, int carry) {
        int b1 = BIN_NUM.indexOf(bit1);
        int b2 = BIN_NUM.indexOf(bit2);

        int sum = b1 + b2 + carry;
        int bitSum = sum & 1;
        int bitCarry = sum >> 1 & 1;

        return new int[]{bitSum, bitCarry};
    }

    private void testBitAdd() {
        assertThat(bitAdd('1', '1', 0), is(new int[]{0, 1}));
        assertThat(bitAdd('1', '1', 1), is(new int[]{1, 1}));
        assertThat(bitAdd('1', '0', 0), is(new int[]{1, 0}));
        assertThat(bitAdd('1', '0', 1), is(new int[]{0, 1}));
        assertThat(bitAdd('0', '0', 0), is(new int[]{0, 0}));
        assertThat(bitAdd('0', '0', 1), is(new int[]{1, 0}));
    }

    private void testBinStringAdd() {
        Integer num1 = 798798343;
        Integer num2 = 907937;
        Integer sum = num1 + num2;
        assertThat(binStringAdd(Integer.toBinaryString(num1), Integer.toBinaryString(num2)),
                is(Integer.toBinaryString(sum)));

        num1 = 1111111111;
        num2 = 11111;
        sum = num1 + num2;
        assertThat(binStringAdd(Integer.toBinaryString(num1), Integer.toBinaryString(num2)),
                is(Integer.toBinaryString(sum)));


    }
}
