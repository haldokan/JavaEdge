package org.haldokan.edge.interviewquest.google;

/**
 * My solution to a Google interview question - Maybe they expect solving it in log(n) using a variation on binary search. I tried
 * that first but it does not seem to lead anywhere
 *
 * The Question: 3-STAR
 *
 * You are given 2 strings which are exactly same but 1 string has an extra character. Find that character.
 */
public class FindExtraCharBetween2Strings {
    public static void main(String[] args) {
        System.out.println(extraChar("abc", "ac"));
        System.out.println(extraChar("ac", "abc"));
        System.out.println(extraChar("ab", "abc"));
        System.out.println(extraChar("abc", "ab"));
        System.out.println(extraChar("bc", "abc"));
        System.out.println(extraChar("abc", "bc"));
        System.out.println(extraChar("ab", "b"));
        System.out.println(extraChar("ab", "a"));
        System.out.println(extraChar("a", "ab"));
        System.out.println(extraChar("b", "ab"));
        System.out.println(extraChar("ba", "ab"));
    }

    static char extraChar(String s1, String s2) {
        int lengthDiff = Math.abs(s1.length() - s2.length());
        if (lengthDiff != 1) {
            throw new IllegalArgumentException(String.format("One string must be longer than the other string by 1 char - found: %d", lengthDiff));
        }
        Character extra = null;
        for (int i = 0; i < s1.length() && extra == null; i++) {
            if (i < s2.length() && s1.charAt(i) != s2.charAt(i)) {
                extra = s1.length() > s2.length() ? s1.charAt(i): s2.charAt(i);
            }
            if (i > s2.length() -1) {
                extra = s1.charAt(i);
            }
        }
        if (extra == null) {
            extra = s2.charAt(s2.length() - 1);
        }
        return extra;
    }
}
