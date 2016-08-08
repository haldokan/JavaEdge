package org.haldokan.edge.interviewquest.bloomberg;


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Bloomberg interview question - O(n^2) complexity
 *
 * The Question: 3_STAR
 * find the longest palindrome in a string.
 * Example:
 * string "AABCDCBA" output should be "ABCDCBA"
 * string "DEFABCBAYT" output should be "ABCBA".
 * <p>
 * Created by haytham.aldokanji on 8/4/16.
 */
public class LongestPalindromeInString {

    public static void main(String[] args) {
        LongestPalindromeInString driver = new LongestPalindromeInString();
        driver.testIsPalindrome();
        driver.testLongestPalindrome();
    }

    public String longestPalindrome(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("str null or empty: " + str);
        }
        if (str.length() == 1) {
            return str;
        }

        String longestPalindrome = "";
        for (int i = 0; i < str.length(); i++) {
            // optimization to avoid extra work when we know that we cannot get a longer palindrome
            if (longestPalindrome.length() > str.length() - i) {
                break;
            }
            String currString = String.valueOf(str.charAt(i));
            for (int j = i + 1; j < str.length(); j++) {
                currString += str.charAt(j);
                if (isPalindrome(currString) && currString.length() > longestPalindrome.length()) {
                    longestPalindrome = currString;
                }
            }
        }
        return longestPalindrome;
    }

    private boolean isPalindrome(String str) {
        String reverse = "";
        for (int i = 0; i < str.length(); i++) {
            reverse = str.charAt(i) + reverse;
        }
        return str.equals(reverse);
    }

    private void testIsPalindrome() {
        assertThat(isPalindrome("racecar"), is(true));
    }

    private void testLongestPalindrome() {
        assertThat(longestPalindrome("AABCDCBA"), is("ABCDCBA"));
        assertThat(longestPalindrome("DEFABCBAYT"), is("ABCBA"));
    }
}
