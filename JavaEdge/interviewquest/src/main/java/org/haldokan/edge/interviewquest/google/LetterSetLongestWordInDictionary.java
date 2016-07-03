package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.Lists;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question
 * The Question: 3_STAR
 * <p>
 * You are given a dictionary, in the form of a file that contains one word per line. E.g.,
 * <p>
 * abacus
 * deltoid
 * gaff
 * giraffe
 * microphone
 * reef
 * qar
 * <p>
 * You are also given a collection of letters. E.g., {a, e, f, f, g, i, r, q}.
 * <p>
 * The task is to find the longest word in the dictionary that can be spelled with the collection of
 * letters. For example, the correct answer for the example values above is “giraffe”. (Note that
 * “reef” is not a possible answer, because the set of letters contains only one “e”.)
 * <p>
 * Created by haytham.aldokanji on 7/2/16.
 */
public class LetterSetLongestWordInDictionary {

    public static void main(String[] args) {
        LetterSetLongestWordInDictionary driver = new LetterSetLongestWordInDictionary();
        List<String> words = Lists.newArrayList("abacus", "deltoid", "gaff", "giraffe", "microphone", "reef", "qar");
        char[] letters = new char[]{'a', 'e', 'f', 'f', 'g', 'i', 'r', 'q'};

        String longestWord = driver.longestWord(words, letters);
        assertThat(longestWord, is("giraffe"));
    }

    public String longestWord(List<String> words, char[] letters) {
        int[] ascii = new int[128];
        int[] rollback = new int[128];

        for (char letter : letters) {
            ascii[letter]++;
        }

        String longest = "";
        for (String word : words) {
            char[] wordLetters = word.toCharArray();
            boolean found = true;

            for (char letter : wordLetters) {
                if (ascii[letter] > 0) {
                    ascii[letter]--;
                    rollback[letter]++;
                } else {
                    found = false;
                    break;
                }
            }
            if (found && word.length() > longest.length()) {
                longest = word;
            }
            for (char letter : wordLetters) {
                ascii[letter] += rollback[letter];
                rollback[letter] = 0;
            }
        }
        return longest;
    }
}
