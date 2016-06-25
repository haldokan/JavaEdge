package org.haldokan.edge.interviewquest.google;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - Space O(1) but time is O(n^2). The O(n) solution is quite involved and is
 * provided on GeeksforGeeks.
 * <p>
 * The Question: 3_STAR
 * <p>
 * You are given a string which has numbers and letters. Numbers occupy all odd positions and letters even positions.
 * You need to transform this string such that all letters move to front of array, and all numbers at the end.
 * The relative order of the letters and numbers needs to be preserved
 * I need to do this in O(n) time and O(1) space.
 * <p>
 * eg: a1b2c3d4 -> abcd1234 , x3y4z6 -> xyz346
 * <p>
 * Created by haytham.aldokanji on 6/25/16.
 */
public class ReorderStringInSitu {
    public static void main(String[] args) {
        ReorderStringInSitu driver = new ReorderStringInSitu();

        char[] str = new char[]{'a', '1', 'b', '2', 'c', '3', 'd', '4', 'e', '5'};
        char[] result = driver.transform(str);
        assertThat(result, is(new char[]{'a', 'b', 'c', 'd', 'e', '1', '2', '3', '4', '5'}));

        str = new char[]{'a', '1', 'b', '2', 'c', '3', 'd', '4', 'e', '5', 'f'};
        result = driver.transform(str);
        assertThat(result, is(new char[]{'a', 'b', 'c', 'd', 'e', 'f', '1', '2', '3', '4', '5'}));

    }

    public char[] transform(char[] str) {
        if (str == null || str.length < 3) {
            return str;
        }
        int start = 1;
        int end = str.length - 1;

        while (end > start) {
            for (int i = start; i < end; i += 2) {
                char tmp = str[i];
                str[i] = str[i + 1];
                str[i + 1] = tmp;
            }
            start++;
            end--;
        }
        return str;
    }
}
