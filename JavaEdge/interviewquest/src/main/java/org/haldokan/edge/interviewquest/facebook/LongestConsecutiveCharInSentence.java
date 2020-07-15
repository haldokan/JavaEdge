package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question
 * The Question: 3_STAR
 * Get longest consecutive chars in a sentence.
 * Examples:
 * heeeello Worldddd => {e, d}
 * <p>
 * Created by haytham.aldokanji on 5/15/16.
 */
public class LongestConsecutiveCharInSentence {

    public static void main(String[] args) {
        LongestConsecutiveCharInSentence driver = new LongestConsecutiveCharInSentence();

        driver.test();
    }

    public List<Character> longestSameCharSeq(String input) {
        List<Character> longestSequences = new ArrayList<>();
        int longestSoFar = 0;
        char[] chars = input.toCharArray();

        int index1 = 0;
        while (index1 < chars.length) {
            char chr = chars[index1];
            int index2 = index1 + 1;

            while (index2 < chars.length && chr == chars[index2]) {
                index2++;
            }
            int currentSeqLen = index2 - index1;
            if (currentSeqLen > longestSoFar) {
                longestSequences.clear();
                longestSoFar = currentSeqLen;
                longestSequences.add(chars[index1]);
            } else if (currentSeqLen == longestSoFar) {
                longestSequences.add(chars[index1]);
            }
            index1 = index2;
        }
        return longestSequences;
    }

    // this other solution is much simpler if we order of chars in the returned results does not have to match the order in the input
    String longestSameCharSeq2(String input) {
        int[] ascii = new int[128];
        int longest = 0;

        for (char chr : input.toCharArray()) {
            ascii[chr] = ascii[chr] + 1;
            if (ascii[chr] > longest) {
                longest = ascii[chr];
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ascii.length; i++) {
            if (ascii[i] == longest) {
                sb.append((char) i).append(",");
            }
        }
        return sb.toString();
    }

    private void test() {
        String sentence = "Heeello Wooorld";
        List<Character> longest = longestSameCharSeq(sentence);
        System.out.println(longest);
        assertThat(longest.size(), is(2));
        assertThat(longest, contains('e', 'o'));

        sentence = "Heeello Wooorlddd";
        longest = longestSameCharSeq(sentence);
        System.out.println(longest);
        assertThat(longest.size(), is(3));
        assertThat(longest, contains('e', 'o', 'd'));

        sentence = "xxxHeeello Wooorlddd";
        longest = longestSameCharSeq(sentence);
        System.out.println(longest);
        assertThat(longest.size(), is(4));
        assertThat(longest, contains('x', 'e', 'o', 'd'));

        sentence = "Howdy Brother";
        longest = longestSameCharSeq(sentence);
        System.out.println(longest);
        assertThat(longest.stream().map(String::valueOf).collect(Collectors.joining()), is(sentence));

        sentence = "Hello Brother";
        longest = longestSameCharSeq(sentence);
        System.out.println(longest);
        assertThat(longest.size(), is(1));
        assertThat(longest, contains('l'));

        sentence = "this is a test sentence";
        longest = longestSameCharSeq(sentence);
        System.out.println(longest);
        assertThat(longest.stream().map(String::valueOf).collect(Collectors.joining()), is(sentence));
    }
}
