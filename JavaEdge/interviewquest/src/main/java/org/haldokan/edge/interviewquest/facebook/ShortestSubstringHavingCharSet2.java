package org.haldokan.edge.interviewquest.facebook;
/**
 * My solution to a Facebook interview question
 * The Question: 4_STAR
 * You are given a set of unique characters and a string.
 * <p>
 * Find the smallest substring of the string containing all the characters in the set.
 * <p>
 * ex:
 * Set : [a, b, c]
 * String : "abbcbcba"
 * <p>
 * Result: "cba"
 * Created by haytham.aldokanji on 5/14/16.
 */
public class ShortestSubstringHavingCharSet2 {
    public static void main(String[] args) {
        System.out.println(shortest("abbcbcba", "abc"));
        System.out.println(shortest("abbcbcxba", "abc"));
        System.out.println(shortest("abdcbcxba", "xbd"));
        System.out.println(shortest("abdcbcxby", "yxa"));
        System.out.println(shortest("abdacbcxby", "yxa"));
        System.out.println(shortest("abdaycbcxby", "yxa"));
        System.out.println(shortest("abdaycbcxby", "yxG")); // should yield null;
    }

    static String shortest(String input, String charset) {
        String shortest = null;
        for (int i = 0; i < input.length(); i++) {
            char chr = input.charAt(i);
            StringBuilder workingCharset = new StringBuilder();
            StringBuilder substring = new StringBuilder();

            if (charset.indexOf(chr) > -1 ) {
                workingCharset.append(chr);
                substring.append(chr);

                int index = i + 1;
                while (index < input.length() && workingCharset.length() < charset.length()) {
                    String currentChar = String.valueOf(input.charAt(index++));
                    if (charset.contains(currentChar) && workingCharset.indexOf(currentChar) < 0) {
                        workingCharset.append(currentChar);
                    }
                    substring.append(currentChar);
                }
                if (workingCharset.length() == charset.length()) { // guard against the while loop finishing due to index while working set is incomplete
                    if (shortest == null || shortest.length() > substring.length()) {
                        shortest = substring.toString();
                    }
                }
            }
        }
        return shortest;
    }
}
