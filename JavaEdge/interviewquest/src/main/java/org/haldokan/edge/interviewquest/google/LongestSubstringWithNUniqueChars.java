package org.haldokan.edge.interviewquest.google;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * This one is NOT my solution but that of someone posted on Careercup. I am putting it here because the concept of what he
 * calls 'histogram' is quite cool!
 * <p>
 * Find longest substring with "m" unique characters in a given string.
 * <p>
 * input: aabacbeabbed
 * output:
 * 4 (aaba) for 2 unique characters
 * 6 (aabacb) for 3 unique characters
 * Created by haytham.aldokanji on 5/23/16.
 */
public class LongestSubstringWithNUniqueChars {
    public static void main(String[] args) {
        LongestSubstringWithNUniqueChars driver = new LongestSubstringWithNUniqueChars();

        String longest = driver.longest("aabacbeabbed", 2);
        assertThat(longest, is("aaba"));

        longest = driver.longest("aabacbeabbed", 3);
        assertThat(longest, is("aabacb"));
    }

    public String longest(String string, int unique) {
        if (unique > string.length()) {
            throw new IllegalArgumentException("Invalid input - unique chars longer than the string: "
                    + string + ", " + unique);
        }
        int stringLen = string.length();
        int[] histogram = new int[128];
        int counter = 0;
        int left = 0, right = 0;
        int from = 0, to = 0;

        while (left < stringLen && right < stringLen) {
            if (counter <= unique) {
                char c = string.charAt(right++);
                if (histogram[c]++ == 0) {
                    counter++;
                }
            } else {
                char c = string.charAt(left++);
                if (histogram[c]-- == 0) {
                    counter--;
                }
            }
            if (to - from < right - left) {
                from = left;
                to = right - 1;
            }
        }
        return string.substring(from, to);
    }
}
