package org.haldokan.edge.interviewquest.linkedin;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * My solution to a Linkedin interview question
 *
 *The Question: 3-STAR
 *
 * Write a program that gives count of common characters presented in an array of strings..(or array of character arrays)
 *
 * For eg.. for the following input strings..
 *
 * aghkafgklt
 * dfghako
 * qwemnaarkf
 *
 * The output should be 3. because the characters a, f and k are present in all 3 strings.
 *
 */
public class CommonCharsInStrings {
    public int commonCharsCount(String[] inputs) {
        int[] counts = new int[128];
        for (String input : inputs) {
            for (char chr : input.toCharArray()) {
                counts[chr] = counts[chr] + 1;
            }
        }
        int totalCount = 0;
        for (int count : counts) {
            if (count >= inputs.length) {
                totalCount++;
            }
        }
        return totalCount;
    }

    @Test
    public void shouldFindCount() {
        assertEquals(4, commonCharsCount(new String[]{"aabbcdefozz", "oabzxyz", "aabbzzo"}));
    }
}
