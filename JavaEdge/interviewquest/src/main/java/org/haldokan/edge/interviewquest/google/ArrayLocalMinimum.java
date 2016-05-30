package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - used binary search with a twist. While it works I think the complexity is
 * still O(n) since in order to find the local minimum we may need to check both halves of the array. I actually don't think
 * this problem can be solved in (logn): example: 10 9 8 7 6 5 4 3 2 0 11, there is no way to pivot toward element 0 from
 * the start using binary search
 * <p>
 * You are given an array of distinct numbers. You need to return an index to a "local minimum" element, which is
 * defined as an element that is smaller than both its adjacent elements. In the case of the array edges, the condition
 * is reduced to one adjacent element.
 * <p>
 * Reach a solution with better time complexity than the trivial solution of O(n).
 * If there are multiple "local minimums", returning any one of them is fine.
 * <p>
 * Created by haytham.aldokanji on 5/30/16.
 */
public class ArrayLocalMinimum {

    public static void main(String[] args) {
        ArrayLocalMinimum driver = new ArrayLocalMinimum();

        driver.test();
    }

    public int localMinimum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Invalid input: " + (arr == null ? null : Arrays.toString(arr)));
        }
        if (arr.length == 1) {
            return 1;
        }
        if (arr[0] < arr[1]) {
            return 0;
        }
        if (arr[arr.length - 1] < arr[arr.length - 2]) {
            return arr.length - 1;
        }
        return doLocalMinimum(arr, 0, arr.length);
    }

    private int doLocalMinimum(int[] arr, int start, int end) {
        int mid = (start + end) / 2;

        if (end - start <= 1) {
            return -1;
        }
        int previous = mid - 1;
        int next = mid + 1;

        if (previous >= 0 && next < arr.length) {
            if (arr[mid] < arr[previous] && arr[mid] < arr[next]) {
                return mid;
            }
        }

        int index = doLocalMinimum(arr, start, mid);
        if (index == -1) {
            index = doLocalMinimum(arr, mid, end);
        }
        return index;
    }

    private void test() {
        int[] arr = new int[]{7, 5, 4, 2, 3, 6};
        int localMinIndex = localMinimum(arr);
        assertThat(localMinIndex, is(3));

        arr = new int[]{7, 5, 4, 3, 2, 1, 0, 8};
        localMinIndex = localMinimum(arr);
        assertThat(localMinIndex, is(6));

        arr = new int[]{7, 5, 4, 3, 2, 1, 0};
        localMinIndex = localMinimum(arr);
        assertThat(localMinIndex, is(6));

        arr = new int[]{7, 5, 4, 2, 3, 6, 1, 8};
        localMinIndex = localMinimum(arr);
        assertThat(localMinIndex, either(equalTo(3)).or(equalTo(6)));

        arr = new int[]{7, 5, 4, 3, 2, 1, 0};
        localMinIndex = localMinimum(arr);
        assertThat(localMinIndex, is(6));

        arr = new int[]{7, 8, 9, 10};
        localMinIndex = localMinimum(arr);
        assertThat(localMinIndex, is(0));
    }
}
