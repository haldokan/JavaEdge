package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.Lists;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - O(N * (M + K)) where N: number of words, M length of word,
 * and K length of letter set.
 * The Question: 3_STAR
 * <p>
 * Given set of characters and a dictionary find the minimum length word that contains all the letters
 * from the given word
 * <p>
 * Created by haytham.aldokanji on 7/17/16.
 */
public class MinDictionaryWordHavingLettersInSet {
    public static void main(String[] args) {
        MinDictionaryWordHavingLettersInSet driver = new MinDictionaryWordHavingLettersInSet();

        List<String> dictionary = Lists.newArrayList("World", "Worlds", "word", "words");

        String minWord = driver.findMinWord(dictionary, new char[]{'d', 'o', 'r', 'w'});
        assertThat(minWord, is("word"));

        minWord = driver.findMinWord(dictionary, new char[]{'d', 'o', 'r', 'w', 'x'});
        assertThat(minWord, nullValue());
    }

    private String findMinWord(List<String> dictionary, char[] letters) {
        boolean[] ascii = new boolean[128];

        String minWord = null;
        for (String word : dictionary) {
            for (char chr : word.toCharArray()) {
                ascii[chr] = true;
            }
            boolean containsAllLetters = true;
            for (char letter : letters) {
                if (!ascii[letter]) {
                    containsAllLetters = false;
                }
                ascii[letter] = false;
            }
            if (containsAllLetters) {
                if (minWord == null || word.length() < minWord.length()) {
                    minWord = word;
                }
            }
        }
        return minWord;
    }
}
