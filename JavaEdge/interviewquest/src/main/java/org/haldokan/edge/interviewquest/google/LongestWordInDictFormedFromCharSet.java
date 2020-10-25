package org.haldokan.edge.interviewquest.google;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - relatively easy question for Google, there must have been some followup
 *
 * The Question: 3.5-STAR
 *
 * You are given a dictionary, in the form of a file that contains one word per line. E.g.,
 * abacus
 * deltoid
 * gaff
 * giraffe
 * microphone
 * reef
 * qar
 * You are also given a collection of letters. E.g., {a, e, f, f, g, i, r, q}.
 * The task is to find the longest word in the dictionary that can be spelled with the collection of
 * letters. For example, the correct answer for the example values above is “giraffe”. (Note that
 * “reef” is not a possible answer, because the set of letters contains only one “e”.)
 *
 * 10/25/20
 */
public class LongestWordInDictFormedFromCharSet {
    public String longestWord(Set<String> dictionary, char[] charset) {
        Map<Character, Integer> occurrenceByCharForCharset = computeCharOccurrenceForCharset(charset);
        String longestWord = null;

        for (String word : dictionary) {
            // weed out upfront a word that's shorter than a previously found word
            if (longestWord == null || word.length() > longestWord.length()) {
                if (canFormWord(word, occurrenceByCharForCharset)) {
                    longestWord = word;
                }
            }
        }
        return longestWord;
    }

    private Map<Character, Integer> computeCharOccurrenceForCharset(char[] charset) {
        Map<Character, Integer> occurrenceByChar = new HashMap<>();
        for (char chr : charset) {
            occurrenceByChar.compute(chr, (k, v) -> v == null ? 1 : v + 1);
        }
        return occurrenceByChar;
    }

    private boolean canFormWord(String word, Map<Character, Integer> occurrenceByCharForCharset) {
        Map<Character, Integer> wordCharOccurrence = new HashMap<>();
        for (char chr : word.toCharArray()) {
            if (!occurrenceByCharForCharset.containsKey(chr)) {
                return false;
            }

            int occurrences = wordCharOccurrence.compute(chr, (k, v) -> v == null ? 1 : v + 1);
            if (occurrences > occurrenceByCharForCharset.get(chr)) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void shortestWord() {
        Set<String> dict = Set.of("abacus", "deltoid", "gaff", "giraffe", "microphone", "reef", "qar");
        char[] charset = new char[]{'a', 'e', 'f', 'f', 'g', 'i', 'r', 'q'};

        assertThat(longestWord(dict, charset), is("giraffe"));
    }
}
