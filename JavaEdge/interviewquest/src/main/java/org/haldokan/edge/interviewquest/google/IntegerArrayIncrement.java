package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;

/**
 * My solution to a Google interview question.
 * <p>
 * Given an integer array of variable length like so [9, 8, 8, 3] where each item in array could be 0 to 9,
 * write a function that would interpret the array [9, 8, 8, 3] as a number 9883 and increment it by 1.
 * The return of the function would be an integer array containing the addition like so [9,8,8,4].
 * No zeros in the first position like [0,1,2,3]. It is not allowed to convert
 * the integer array to String then convert to Integer or Long and then do the addition of 1 and then convert it back
 * to integer array.
 * <p>
 * Created by haytham.aldokanji on 9/21/15.
 */
public class IntegerArrayIncrement {
    public static void main(String[] args) {
        IntegerArrayIncrement driver = new IntegerArrayIncrement();
        System.out.println(Arrays.toString(driver.increment(new int[]{9, 8, 8, 3})));
        System.out.println(Arrays.toString(driver.increment(new int[]{9, 9, 9})));
        System.out.println(Arrays.toString(driver.increment(new int[]{9})));
        System.out.println(Arrays.toString(driver.increment(new int[]{0})));
    }

    public int[] increment(int[] a) {
        if (a == null || a.length == 0)
            throw new IllegalArgumentException("Invalid input");

        int num = 0;
        for (int i = 0; i < a.length; i++) {
            num += a[a.length - i - 1] * Math.pow(10, i);
        }
        num++;
        int[] result = new int[(int) Math.log10(num) + 1];
        for (int i = 0; i < result.length; i++) {
            result[result.length - i - 1] = num % 10;
            num /= 10;
        }
        return result;
    }
}
