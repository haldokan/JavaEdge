package org.haldokan.edge.interviewquest.bloomberg;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Bloomberg interview question
 * The Question: 2_STAR
 * <p>
 * Given an array A = [3, 7, 2 , 5, 6, 4] for a number N, print the pairs from that array A that sums up to N.
 * You should print each pair once.
 * <p>
 * Created by haytham.aldokanji on 8/5/16.
 */
public class ItemsAddingToSumInArray {

    public static void main(String[] args) {
        ItemsAddingToSumInArray driver = new ItemsAddingToSumInArray();

        List<int[]> result = driver.parisAddingToSum(new int[]{3, 7, 2, 5, 6, 4}, 10);
        assertThat(result.size(), is(2));
        assertThat(result.get(0), is(new int[]{3, 7}));
        assertThat(result.get(1), is(new int[]{6, 4}));

        result = driver.parisAddingToSum(new int[]{3, 7, 2, 5, 6, 4}, 9);
        assertThat(result.size(), is(3));
        assertThat(result.get(0), is(new int[]{3, 6}));
        assertThat(result.get(1), is(new int[]{7, 2}));
        assertThat(result.get(2), is(new int[]{5, 4}));
    }

    public List<int[]> parisAddingToSum(int[] arr, int sum) {
        List<int[]> result = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] + arr[j] == sum) {
                    result.add(new int[]{arr[i], arr[j]});
                }
            }
        }
        return result;
    }
}
