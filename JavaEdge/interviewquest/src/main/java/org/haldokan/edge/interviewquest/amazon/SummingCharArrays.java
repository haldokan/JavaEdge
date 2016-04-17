package org.haldokan.edge.interviewquest.amazon;

import java.util.Arrays;

/**
 * My solution to an Amazon interview question
 * <p>
 * Write a function that accepts two character arrays each represents a floating point number and return their sum in character array.
 * For example function accepts "23.45" and "2.5" and return their sum "25.95".
 * Restriction: We cannot use predefined functions / methods or parsing. We have to go with basic operations.
 */
public class SummingCharArrays {
    private static final int ZERO_ASCII_CODE = 0X30;

    public static void main(String[] args) {
        SummingCharArrays driver = new SummingCharArrays();
        driver.test1();
        driver.test2();
        driver.test3();
        driver.test4();
        driver.test5();
        driver.test6();
    }

    public char[] sum(char[] arr1, char[] arr2) {
        char[][] arr1Parts = split(arr1);
        char[][] arr2Parts = split(arr2);

        int[] arr1Decimal = convertToIntArr(arr1Parts[1]);
        int[] arr2Decimal = convertToIntArr(arr2Parts[1]);
        int[] sumDecimal = doSum(arr1Decimal, arr2Decimal, 0, ArrayAlign.LEFT);
        char[] decimal = convertToCharArr(sumDecimal);

        int[] arr1Integral = convertToIntArr(arr1Parts[0]);
        int[] arr2Integral = convertToIntArr(arr2Parts[0]);
        int carryFromDecimal = sumDecimal[0];
        int[] sumIntegral = doSum(arr1Integral, arr2Integral, carryFromDecimal, ArrayAlign.RIGHT);
        char[] integral = convertToCharArr(sumIntegral);

        // decimal will always have the carry val
        int rsltLen = decimal.length - 1 + integral.length;
        int carryFromIntegral = sumIntegral[0];

        int integralIndex = 0;
        // exclude the carry
        int decimalIndex = 1;

        if (carryFromIntegral == 0) {
            rsltLen--;
            integralIndex++;
        }
        // we need a place for the decimal point
        if (decimal.length > 1) {
            rsltLen++;
        }

        char[] rslt = new char[rsltLen];
        int integralLen = integral.length - integralIndex;
        System.arraycopy(integral, integralIndex, rslt, 0, integralLen);

        if (decimal.length > 1) {
            rslt[integralLen] = '.';
            System.arraycopy(decimal, decimalIndex, rslt, integralLen + 1, decimal.length - decimalIndex);
        }
        return rslt;
    }

    private int[] convertToIntArr(char[] charArr) {
        int[] intArr = new int[charArr.length];
        for (int i = 0; i < charArr.length; i++) {
            // the char is casted to int we get its ascii value. Here we are getting the number corresponding to
            // the numeric char by &ing it to 0XF
            intArr[i] = charArr[i] & 0XF;
        }
        return intArr;
    }

    private char[] convertToCharArr(int[] intArr) {
        char[] charArr = new char[intArr.length];
        for (int i = 0; i < intArr.length; i++) {
            charArr[i] = (char) (intArr[i] | ZERO_ASCII_CODE);
        }
        return charArr;
    }

    private char[][] split(char[] arr) {
        int decimalPointIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '.') {
                decimalPointIndex = i;
                break;
            }
        }
        char[][] rslt;
        if (decimalPointIndex == 0) {
            char[] integral = new char[arr.length];
            System.arraycopy(arr, 0, integral, 0, arr.length);
            rslt = new char[][]{integral, new char[]{}};
        } else {
            char[] integral = new char[decimalPointIndex];
            char[] decimal = new char[arr.length - decimalPointIndex - 1];

            System.arraycopy(arr, 0, integral, 0, decimalPointIndex);
            System.arraycopy(arr, decimalPointIndex + 1, decimal, 0, arr.length - decimalPointIndex - 1);
            rslt = new char[][]{integral, decimal};
        }
        return rslt;
    }

    private int[] doSum(int[] arr1, int[] arr2, int carry, ArrayAlign align) {
        int[][] aligned = alignArrays(arr1, arr2, align);

        int[] aligned1 = aligned[0];
        int[] aligned2 = aligned[1];

        int[] rslt = new int[aligned1.length + 1];
        Arrays.fill(rslt, 0);

        for (int i = rslt.length - 1; i > 0; i--) {
            int[] sum = sumDigitsAtIndex(aligned1, aligned2, i - 1, carry);
            carry = sum[0];
            rslt[i] = sum[1];
        }
        rslt[0] = carry;
        return rslt;
    }

    private int[][] alignArrays(int[] arr1, int[] arr2, ArrayAlign align) {
        int maxLen = arr1.length > arr2.length ? arr1.length : arr2.length;

        if (align == ArrayAlign.RIGHT) {
            return new int[][]{alignRight(arr1, maxLen), alignRight(arr2, maxLen)};
        } else {
            return new int[][]{alignLeft(arr1, maxLen), alignLeft(arr2, maxLen)};
        }
    }

    private int[] alignRight(int[] arr, int len) {
        int[] aligned = new int[len];
        Arrays.fill(aligned, 0);

        int index = len;
        for (int i = arr.length - 1; i >= 0; i--) {
            aligned[--index] = arr[i];
        }
        return aligned;
    }

    private int[] alignLeft(int[] arr, int len) {
        int[] aligned = new int[len];
        Arrays.fill(aligned, 0);

        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            aligned[index++] = arr[i];
        }
        return aligned;
    }

    private int[] sumDigitsAtIndex(int[] arr1, int[] arr2, int index, int carry) {
        int[] rslt = new int[2];
        int newCarry = 0;
        int sum = arr1[index] + arr2[index] + carry;

        if (sum >= 10) {
            sum -= 10;
            newCarry = 1;

        }
        rslt[0] = newCarry;
        rslt[1] = sum;

        return rslt;
    }

    private void test1() {
        int[] arr1 = new int[]{1, 3, 2};
        int[] arr2 = new int[]{1, 0, 0, 2};

        System.out.println(Arrays.toString(doSum(arr1, arr2, 0, ArrayAlign.RIGHT)));

        arr1 = new int[]{9, 2};
        arr2 = new int[]{1, 0};

        System.out.println(Arrays.toString(doSum(arr1, arr2, 0, ArrayAlign.RIGHT)));
    }

    private void test2() {
        char[] arr1 = new char[]{'1', '2', '.', '5', '3'};
        char[] arr2 = new char[]{'1', '2', '5', '3'};

        char[][] arr1Parts = split(arr1);
        System.out.println("arr1->" + Arrays.toString(arr1Parts[0]));
        System.out.println("arr1->" + Arrays.toString(arr1Parts[1]));

        char[][] arr2Parts = split(arr2);
        System.out.println("arr2->" + Arrays.toString(arr2Parts[0]));
        System.out.println("arr2->" + Arrays.toString(arr2Parts[1]));
    }

    private void test3() {
        char[] arr = new char[]{'1', '2', '5', '3'};
        System.out.println("test3->" + Arrays.toString(convertToIntArr(arr)));
    }

    private void test4() {
        char[] arr1 = new char[]{'1', '2', '.', '5', '3'};
        char[] arr2 = new char[]{'2', '.', '5'};
        char[] arr3 = new char[]{'2', '.', '4'};

        char[] rslt = sum(arr1, arr2);
        System.out.println("test4->" + Arrays.toString(rslt));

        rslt = sum(arr1, arr3);
        System.out.println("test4->" + Arrays.toString(rslt));
    }

    private void test5() {
        char[] arr1 = new char[]{'1', '2', '.', '5', '3'};
        char[] arr2 = new char[]{'1', '0'};

        char[] rslt = sum(arr1, arr2);
        System.out.println("test5->" + Arrays.toString(rslt));
    }

    private void test6() {
        char[] arr1 = new char[]{'1', '2', '5'};
        char[] arr2 = new char[]{'1', '0'};

        char[] rslt = sum(arr1, arr2);
        System.out.println("test6->" + Arrays.toString(rslt));
    }

    private enum ArrayAlign {LEFT, RIGHT}

}
