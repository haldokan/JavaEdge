package org.haldokan.edge.interviewquest.facebook;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Facebook interview question
 * Note: this is a horrible solution with exponential cost - check MatchingStringWithPlaceHolders2 which uses a trie
 * for every word we have to make all subsets of '*' from 1 - word.len and
 * mask the word with the subset (replace letters with '*'). Constructing subsets has O(2^n) complexity and is done using
 * backtracking algorithms. Figuring out how to construct the backtracking machinery is tricky. The solution here draws
 * on the the org.haldokan.edge.dynamicprog.AllSubSetsInSet.java I present in this repo which in turn is a port from a C
 * implementation presented by Steven S. Skiena in his book Algorithm Design Manual.
 * The Question: 4_STAR
 * Write a program that answers YES/NO search queries containing * placeholders (this is not regex)
 * Example: if the data you have is (hazem, ahmed, moustafa, fizo), then you should answer as follows for:
 *  ahmed: YES
 *  m**stafa: YES
 *  fizoo: NO
 *  fizd: NO
 *  *****: YES
 *  ****: YES
 * *: NO
 *  Your program should be able to answer each search query in O(1).
 * <p>
 * Created by haytham.aldokanji on 5/13/16.
 */
public class MatchingStringWithPlaceHolders {
    private final Set<String> wordsWithPlaceHolders = new HashSet<>();

    public static void main(String[] args) {
        MatchingStringWithPlaceHolders driver = new MatchingStringWithPlaceHolders();

        String[] words = new String[]{"ever", "Saturday", "and", "Sunday", "I", "run"};
        driver.createWordsWithPlaceHolders(words);

        System.out.println("Size of word collection -> " + words.length);
        System.out.println("Size of word collection with placeholders -> " + driver.wordsWithPlaceHolders.size());

        driver.test();
    }

    public boolean exists(String word) {
        return wordsWithPlaceHolders.contains(word);
    }

    private void createWordsWithPlaceHolders(String[] words) {
        for (String word : words) {
            wordsWithPlaceHolders.add(word);
            // add 1 to array length to simplify solution
            int[] combinations = new int[word.length() + 1];
            // use backtracking to get all subsets
            backtrack(combinations, 0, word);
        }
    }

    // create all subsets of 1 - dataLen: concise, tricky and clever!
    public void backtrack(int[] combinations, int currentSolutionLen, String word) {
        int[] c = new int[2];
        int numCandidates;

        if (currentSolutionLen == word.length()) {
            addToWordCollection(combinations, currentSolutionLen, word);
        } else {
            currentSolutionLen = currentSolutionLen + 1;
            numCandidates = candidates(combinations, currentSolutionLen, word.length(), c);
            for (int i = 0; i < numCandidates; i++) {
                combinations[currentSolutionLen] = c[i];
                // makeMove(a, k, input)
                backtrack(combinations, currentSolutionLen, word);
                // unmakeMove(a, k, input)
            }
        }
    }

    private int candidates(int[] combinations, int k, int dataLen, int[] candidates) {
        candidates[0] = 1;
        candidates[1] = 0;
        return 2;
    }

    private void addToWordCollection(int[] combinations, int solutionLen, String word) {
        StringBuilder wordBuilder = new StringBuilder(word);
        for (int i = 1; i <= solutionLen; i++) {
            if (combinations[i] == 1) {
                wordBuilder.setCharAt(i - 1, '*');
                wordsWithPlaceHolders.add(wordBuilder.toString());
            }
        }
    }

    private void test() {
        assertThat(exists("Saturday"), is(true));
        assertThat(exists("S*turday"), is(true));
        assertThat(exists("Satu**ay"), is(true));
        assertThat(exists("Satur***"), is(true));
        assertThat(exists("**turday"), is(true));
        assertThat(exists("S******y"), is(true));
        assertThat(exists("********"), is(true));
        assertThat(exists("*****da*"), is(true));
        assertThat(exists("*****d**"), is(true));
        assertThat(exists("**t**d**"), is(true));
        assertThat(exists("run"), is(true));
        assertThat(exists("r*n"), is(true));
        assertThat(exists("*"), is(true));
        assertThat(exists("**t**d***"), is(false));
        assertThat(exists("**T**d*"), is(false));
    }
}
