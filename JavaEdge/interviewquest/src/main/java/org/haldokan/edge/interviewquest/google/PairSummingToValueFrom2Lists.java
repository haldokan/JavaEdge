package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google inteview question - time complexity is O(n)
 * <p>
 * The Question: 3_STAR
 * <p>
 * Given two integer arrays list1 and list2 and an int target value. Write an app to check if there exists such a sum, where one
 * number taken from list1 and other from list2 to add up to become the target value. Return true if so, else return false.
 * <p>
 * Created by haytham.aldokanji on 9/13/16.
 */
public class PairSummingToValueFrom2Lists {

    public static void main(String[] args) {
        PairSummingToValueFrom2Lists driver = new PairSummingToValueFrom2Lists();
        driver.test();
    }

    public Optional<Integer[]> pair(Integer[] arr1, Integer[] arr2, Integer sum) {
        Set<Integer> set1 = Arrays.stream(arr1).collect(Collectors.toSet());

        for (Integer val : arr2) {
            Integer operand = sum - val;
            if (set1.contains(operand)) {
                return Optional.of(new Integer[]{operand, val});
            }
        }
        return Optional.empty();
    }

    private void test() {
        Integer[] arr1 = new Integer[]{1, 3, 11, 5, 7, 7, 8, 8};
        Integer[] arr2 = new Integer[]{1, 4, 9, 6, 8, 8, 4};

        Integer[] result = pair(arr1, arr2, 19).get();
        assertThat(result, is(new Integer[]{11, 8}));

        result = pair(arr1, arr2, 4).get();
        assertThat(result, is(new Integer[]{3, 1}));

        assertThat(pair(arr1, arr2, 21).isPresent(), is(false));
    }
}
