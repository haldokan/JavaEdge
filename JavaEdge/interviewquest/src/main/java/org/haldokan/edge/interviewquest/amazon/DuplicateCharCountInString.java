package org.haldokan.edge.interviewquest.amazon;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to an Amazon interview question
 * The Question: 2_STAR
 * <p>
 * Print the count of duplicate char in a given string in same order. Ex: Input- 'abbaccdbac', Output- 'a3b3c3d1'
 * <p>
 * Created by haytham.aldokanji on 7/6/16.
 */
public class DuplicateCharCountInString {
    public static void main(String[] args) {
        String result = dupCount("abbaccdbac");
        System.out.printf("%s%n", result);
        assertThat(result, is("a3b3c3d1"));
    }

    public static String dupCount(String string) {
        //assume printable chars
        int[] ascii = new int[128];
        for (char chr : string.toCharArray()) {
            ascii[chr]++;
        }
        StringBuilder result = new StringBuilder();
        for (char chr : string.toCharArray()) {
            if (ascii[chr] > 0) {
                result.append(chr).append(ascii[chr]);
                ascii[chr] = 0;
            }
        }
        return result.toString();
    }
}
