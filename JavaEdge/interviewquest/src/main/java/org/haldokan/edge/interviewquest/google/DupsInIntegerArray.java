package org.haldokan.edge.interviewquest.google;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - in situ with O(n) time
 *
 * The Question: 2_STAR
 * <p>
 * Find the duplicate in an array of length n. The values are positive integers in the range between 1 .. n-1.
 * <p>
 * The way I understand the is question is that all numbers in the range must be present. Since the range in 1 to n -1 there will
 * always be an exactly one dup to fill the array of length n.
 *
 * @author haldokan
 */
public class DupsInIntegerArray {

    public static void main(String[] args) {
        DupsInIntegerArray driver = new DupsInIntegerArray();

        int[] arr = new int[]{4, 3, 1, 2, 3};
        assertThat(driver.duplicate(arr), is(3));

        arr = new int[]{4, 3, 1, 2, 9, 5, 8, 6, 7, 9};
        assertThat(driver.duplicate(arr), is(9));

        arr = new int[]{1, 4, 3, 1, 2, 9, 5, 8, 6, 7};
        assertThat(driver.duplicate(arr), is(1));
    }

    public int duplicate(int[] arr) {
        int dup = 0;
        for (int i = 0; i < arr.length; i++) {
            int valAtIndex = arr[arr[0]];

            if (arr[0] == valAtIndex) {
                dup = arr[0];
            }
            int tmp = arr[0];
            arr[0] = valAtIndex;
            arr[tmp] = tmp;
        }
        return dup;
    }
}
