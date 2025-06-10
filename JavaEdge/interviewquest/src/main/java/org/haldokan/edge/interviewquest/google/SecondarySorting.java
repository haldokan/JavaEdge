package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question
 * The Question: 2_STAR
 * <p>
 * how would you implement a secondary sorting. Meaning sorting by Category A, and then sub sorting by category B?
 * <p>
 * Created by haytham.aldokanji on 7/3/16.
 */
public class SecondarySorting {
    public static void main(String[] args) {
        String[] words = new String[]{"zzxxy", "mlno", "xyz", "lmno", "zaxbl"};
        sort(words);
        assertThat(words, is(new String[]{"xyz", "lmno", "mlno", "zaxbl", "zzxxy"}));
    }

    // sort list of words by their length and char lexical order
    public static void sort(String[] words) {
        Arrays.sort(words, (s1, s2) -> {
            if (s1.length() == s2.length()) {
                return s1.compareTo(s2);
            } else {
                return s1.length() - s2.length();
            }
        });
    }
}
