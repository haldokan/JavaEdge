package org.haldokan.edge.interviewquest.amazon;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to an Amazon interview question
 * The Question: 3_STAR
 * <p>
 * Print all the characters present in the given string only once in a reverse order. Time & Space complexity should
 * not be more than O(N).
 * e.g.
 * 1)Given a string aabdceaaabbbcd
 * the output should be - dcbae
 * <p>
 * 2)Sample String - aaaabbcddddccbbdaaeee
 * Output - eadbc
 * <p>
 * 3)I/P - aaafffcccddaabbeeddhhhaaabbccddaaaa
 * O/P - adcbhef
 * Created by haytham.aldokanji on 7/6/16.
 */
public class ReverseStringRemovingDups {

    public static void main(String[] args) {
        assertThat(reverse("aaafffcccddaabbeeddhhhaaabbccddaaaa"), is("adcbhef"));
        assertThat(reverse("aaaabbcddddccbbdaaeee"), is("eadbc"));
        assertThat(reverse("aabdceaaabbbcd"), is("dcbae"));
    }

    public static String reverse(String string) {
        if (string == null) {
            throw new NullPointerException("Null parameter passed");
        }
        StringBuilder result = new StringBuilder();
        //assume -reasonably- string has only printable chars
        char[] ascii = new char[128];
        for (int i = string.length() - 1; i >= 0; i--) {
            char chr = string.charAt(i);
            if (ascii[chr]++ == 0) {
                result.append(chr);
            }
        }
        return result.toString();
    }
}
