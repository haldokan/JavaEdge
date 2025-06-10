package org.haldokan.edge.interviewquest.amazon;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * Given a read only array of n + 1 integers between 1 and n, find one number that repeats in linear time using
 * less than O(n) space and traversing the stream sequentially O(1) times.
 * <p>
 * Created by haytham.aldokanji on 7/20/16.
 */
public class RepeatedNumInOneToNArray {

    public static void main(String[] args) {
        int[] arr = new int[]{5, 4, 3, 2, 1, 6, 7, 10, 9, 8, 7};
        assertThat(findRepeated(arr), is(7));
    }

    public static int findRepeated(int[] arr) {
        return Arrays.stream(arr).sum() - ((arr.length - 1) * (arr.length) / 2);
        // 1, 2 -> 3
        // 1, 2, 3 -> 6
        // 1, 2, 3, 4 -> 10
        // 1, 2, 3, 4, 5 -> 15
        // 1, 2, 3, 4, 5, 6 -> 21
        // (6 * 5)/2 = 15
        // (4 * 5)/2 = 10
        // (3 * 4) = 6
    }
}
