package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question
 * <p>
 * Array of size (n-m) with numbers from 1..n with m of them missing. Find all of the missing numbers in O(logn).
 * Array is sorted.
 * <p>
 * Example:
 * n = 8
 * arr = [1,2,4,5,6,8]
 * m = 2
 * Result has to be a set {3, 7}.
 * <p>
 * Created by haytham.aldokanji on 5/11/16.
 */
public class MissingNumbersInSortedArray {

    public static void main(String[] args) {
        MissingNumbersInSortedArray driver = new MissingNumbersInSortedArray();

        int[] arr = new int[]{1, 2, 4, 5, 6, 8, 10, 12, 13, 15, 16, 18, 21};
        List<Integer> missing = driver.missing(arr);
        assertThat(missing, containsInAnyOrder(3, 7, 9, 11, 14, 17, 19, 20));
    }

    public List<Integer> missing(int[] arr) {
        List<Integer> missing = new ArrayList<>();
        missing(arr, 0, arr.length, missing);
        return missing;
    }

    private void missing(int[] arr, int start, int end, List<Integer> missingNums) {
        int mid = (start + end) / 2;

        if (end - start <= 1) {
            return;
        }
        int afterMid = mid + 1;
        if (afterMid < arr.length) {
            int biggerVal = arr[afterMid];
            int smallerVal = arr[mid];

            if (biggerVal - smallerVal > 1) {
                for (int i = smallerVal + 1; i < biggerVal; i++) {
                    missingNums.add(i);
                }
            }
        }
        missing(arr, start, mid, missingNums);
        missing(arr, mid, end, missingNums);
    }
}
