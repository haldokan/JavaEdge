package org.haldokan.edge.interviewquest.bloomberg;

/**
 * Some shitty interview question!
 * <p>
 * find the longest palindrome in a string.
 * Example:
 * string "AABCDCBA" output should be "ABCDCBA"
 * string "DEFABCBAYT" output should be "ABCBA".
 * <p>
 * Created by haytham.aldokanji on 8/4/16.
 */
public class LongestPalindromeInString {

    public String longestPalindrome(String str) {
        char[] strChars = str.toCharArray();
        char[] reverseStrChars = new char[strChars.length];

        int index = strChars.length - 1;
        for (char chr : strChars) {
            reverseStrChars[index--] = chr;
        }
        return null;
    }
}
