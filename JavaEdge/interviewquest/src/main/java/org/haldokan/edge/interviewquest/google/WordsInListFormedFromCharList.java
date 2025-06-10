package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - Space O(1) and time O(n) where n is the number of chars in
 * the words list. More precisely we scan each word of length m 2 times -> 2 * n.
 * The Question: 3_STAR
 * <p>
 * Write a program to return list of words (from the given list of words) that can be formed from the list of
 * given characters.
 * <p>
 * This is like Scribble game. Say for example the given characters are ('a' , 'c' , 't' }
 * and list the words are {'cat', 'act', 'ac', 'stop' , 'cac'}.
 * <p>
 * The program should return only the words that
 * can be formed from given letters. Here the output should be {'cat', 'act', 'ac'}.
 * <p>
 * Created by haytham.aldokanji on 6/25/16.
 */
public class WordsInListFormedFromCharList {

    public static void main(String[] args) {
        WordsInListFormedFromCharList driver = new WordsInListFormedFromCharList();

        char[] chars = new char[]{'a', 'c', 't', 'o'};
        String[] words = new String[]{"cat", "stop", "act", "cac", "ac", "taco", "too"};
        List<String> result = driver.wordList(chars, words);
        System.out.printf("%s%n", result);
        assertThat(result.toArray(), is(new String[]{"cat", "act", "ac", "taco"}));
    }


    public List<String> wordList(char[] chars, String[] words) {
        // assuming printable ascii chars
        int[] ascii = new int[128];
        int[] tracker = new int[ascii.length];
        for (char chr : chars) {
            ascii[chr]++;
        }

        List<String> result = new ArrayList<>();
        for (String word : words) {
            char[] wordChars = word.toCharArray();
            boolean include = true;

            for (char chr : wordChars) {
                if (ascii[chr] > 0) {
                    ascii[chr]--;
                    tracker[chr]++;
                } else {
                    include = false;
                    break;
                }
            }
            if (include) {
                result.add(word);
            }
            for (char chr : wordChars) {
                if (tracker[chr] > 0) {
                    ascii[chr] += tracker[chr];
                    tracker[chr] = 0;
                }
            }
        }
        return result;
    }
}
