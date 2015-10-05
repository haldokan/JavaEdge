package org.haldokan.edge.interviewquest.google;

/**
 * My solution to a Google interview question. A popular num would be one of those at index 0, len/4, len/2, 3len/4
 * Find popular item in sorted array of natural numbers.
 * An item is popular is if its repeated n/4 times or more.
 * For Ex:
 * Input: 123444445678
 * Popular item is 4.
 * <p>
 * Linier scan is O(n), but solution needs to be in O(logN) complexity and O(1) space complexity.
 * Created by haytham.aldokanji on 10/5/15.
 */
public class PopularNumInSortedArray {

    public static void main(String[] args) {
        PopularNumInSortedArray driver = new PopularNumInSortedArray();
        int[] arr = new int[]{1, 2, 3, 4, 4, 4, 4, 4, 5, 6, 7, 8};
        System.out.println(driver.popularNum(arr));

        int[] arr2 = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        System.out.println(driver.popularNum(arr2));
    }

    public int popularNum(int[] arr) {
        for (int i = 0; i < arr.length; i += arr.length / 4) {
            int val = arr[i];
            int reps = 0;
            for (int j = i; j < arr.length && arr[j] == val; j++, reps++) {
                if (reps >= arr.length / 4)
                    return val;
            }
        }
        return -1;
    }
}
