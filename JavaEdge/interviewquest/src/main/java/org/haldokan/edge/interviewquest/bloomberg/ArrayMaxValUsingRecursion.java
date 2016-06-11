package org.haldokan.edge.interviewquest.bloomberg;

/**
 * My solution to a JET (a company located in Hoboken/NJ) interview question - grouped with Bloomberg because it is at
 * the same level of complexity of a Bloomberg interview question
 * The Question: 3_STAR
 * Find out the max value in an array using recursion
 * <p>
 * Created by haytham.aldokanji on 4/27/16.
 */
public class ArrayMaxValUsingRecursion {

    public static void main(String[] args) {
        System.out.println(max(new int[]{3, 1, 0, 8, 6, 2, 10, 1, 3}));
        System.out.println(max(new int[]{3, 5}));
        System.out.println(max(new int[]{6, 5}));
        System.out.println(max(new int[]{3}));
    }

    public static int max(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Invalid input");
        }
        return doMax(arr, 0, arr[0]);
    }

    private static int doMax(int[] arr, int index, int max) {
        if (index == arr.length) {
            return max;
        } else {
            if (arr[index] > max) {
                return doMax(arr, index + 1, arr[index]);
            } else {
                return doMax(arr, index + 1, max);
            }
        }
    }
}
