package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * My solution to a Facebook interview question - silly question!
 * The Question: 1_STAR
 * Code a function that receives an array with duplicates and returns a new array keeping the original order of
 * the elements but with the duplicates removed.
 * <p>
 * <p>
 * For example, if the input were
 *
 * @[ @"dog", @"cat", @"dog", @"fish" ]
 * the output would be
 * @[ @"dog", @"cat", @"fish" ]
 * Tell the complexity of the solution.
 * Created by haytham.aldokanji on 5/6/16.
 */
public class RemoveDupsFromArray {
    public static void main(String[] args) {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 2, 2, 3, 5, 3, 6, 1, 7};
        Integer[] unique = removeDups(arr);
        System.out.println(Arrays.toString(unique));
    }

    public static Integer[] removeDups(Integer[] arr) {
        Set<Integer> unique = new LinkedHashSet<>();
        Arrays.stream(arr).forEach(unique::add);
        return unique.stream().toArray(Integer[]::new);
    }
}
