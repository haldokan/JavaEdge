package org.haldokan.edge.interviewquest.google;

import java.util.function.Predicate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - while the solution is O(1) space it is O(n^2) time. I don't think there
 * is a solution in O(n) time for this problem
 * <p>
 * Given an array of positive and negative numbers, arrange them in an alternate fashion such that every positive number
 * is followed by negative and vice-versa maintaining the order of appearance.
 * Number of positive and negative numbers need not be equal. If there are more positive numbers they appear at the end
 * of the array. If there are more negative numbers, they too appear in the end of the array.*
 * <p>
 * Example:
 * <p>
 * <p>
 * Input:  arr[] = {1, 2, 3, -4, -1, 4}
 * Output: arr[] = {-4, 1, -1, 2, 3, 4}
 * <p>
 * Input:  arr[] = {-5, -2, 5, 2, 4, 7, 1, 8, 0, -8}
 * output: arr[] = {-5, 5, -2, 2, -8, 4, 7, 1, 8, 0}
 * <p>
 * Limitations:
 * a) Use O(1) extra space
 * b) Time Complexity should be O(N)
 * c) Maintain the order of appearance of elements as in original array.
 * <p>
 * Created by haytham.aldokanji on 6/5/16.
 */
public class AlternatePosAndNegNumsKeepingOrder {

    public static void main(String[] args) {
        AlternatePosAndNegNumsKeepingOrder driver = new AlternatePosAndNegNumsKeepingOrder();

        driver.test();
    }

    public void reorder(int[] arr) {
        if (arr == null || arr.length < 3) {
            return;
        }
        for (int i = 1; i < arr.length; i++) {
            if (!(arr[i] >= 0 && arr[i - 1] < 0 || arr[i] < 0 && arr[i - 1] >= 0)) {
                int index = -1;
                if (arr[i] < 0 && arr[i - 1] < 0) {
                    index = next(arr, i + 1, v -> v >= 0);
                } else if (arr[i] >= 0 && arr[i - 1] >= 0) {
                    index = next(arr, i + 1, v -> v < 0);
                }
                if (index == -1) {
                    return;
                }
                for (int j = index; j > i; j--) {
                    int tmp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
    }

    public int next(int[] arr, int start, Predicate<Integer> evaluator) {
        for (int i = start; i < arr.length; i++) {
            if (evaluator.test(arr[i])) {
                return i;
            }
        }
        return -1;
    }

    private void test() {
        int[] arr = new int[]{1, 2, 3, -4, -1, 4};
        reorder(arr);
        assertThat(arr, is(new int[]{1, -4, 2, -1, 3, 4}));

        arr = new int[]{-5, -2, 5, 2, 4, 7, 1, 8, 0, -8};
        reorder(arr);
        assertThat(arr, is(new int[]{-5, 5, -2, 2, -8, 4, 7, 1, 8, 0}));
    }
}
