package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - used dynamic programming drawing on a more elaborate algorithm for
 * approximate search presented by S. Skiena's book Algorithm Design Manual. I present a Java implementation of the
 * approximate search algorithm in this repo under org.haldokan.edge.dynamicprog.ApproximateStringMatcher
 * <p>
 * Implement a function OneEditApart with the following signature:
 * bool OneEditApart(string s1, string s2)
 * Edit is: insertion, removal, replacement
 * <p>
 * OneEditApart("cat", "dog") = false
 * OneEditApart("cat", "cats") = true
 * OneEditApart("cat", "cut") = true
 * OneEditApart("cat", "cast") = true
 * OneEditApart("cat", "at") = true
 * OneEditApart("cat", "acts") = false
 * <p>
 * Created by haytham.aldokanji on 5/7/16.
 */
public class WordsOneEditApart {

    public static void main(String[] args) {
        WordsOneEditApart driver = new WordsOneEditApart();
        driver.test();
    }

    public boolean isOneEditApart(String word1, String word2) {
        String word1Copy = " " + word1;
        String word2Copy = " " + word2;
        char[] word1chars = word1Copy.toCharArray();
        char[] word2chars = word2Copy.toCharArray();

        int[][] editTable = new int[word1chars.length][word2chars.length];
        initEditTable(editTable);

        for (int i = 1; i < word1chars.length; i++) {
            for (int j = 1; j < word2chars.length; j++) {
                int matchCost = editTable[i - 1][j - 1] + match(word1chars[i], word2chars[j]);
                int insertCost = editTable[i - 1][j] + modify(word1chars[i]);
                int deleteCost = editTable[i][j - 1] + modify(word2chars[j]);

                int[] costs = new int[]{matchCost, insertCost, deleteCost};
                Arrays.sort(costs);

                editTable[i][j] = costs[0];
            }
        }
//        printEditTable(editTable);
        return editTable[word1chars.length - 1][word2chars.length - 1] == 1;
    }

    private void initEditTable(int[][] table) {
        int[] firstRow = table[0];
        for (int j = 0; j < firstRow.length; j++) {
            //cost of insert to turn first letter of 1st word to each of the letter in the 2nd
            firstRow[j] = j;
        }

        for (int i = 0; i < table.length; i++) {
            //cost of delete to turn first letter of 2nd word to each of the letter in the 1st
            table[i][0] = i;
        }
    }

    private int match(char c1, char c2) {
        return c1 == c2 ? 0 : 1;
    }

    private int modify(char c) {
        return 1;
    }

    private void printEditTable(int[][] editTable) {
        for (int[] row : editTable) {
            System.out.println(Arrays.toString(row));
        }
    }

    private void test() {
        String word1 = "cat";
        String word2 = "cats";
        assertThat(isOneEditApart(word1, word2), is(true));

        word1 = "The product has a shelf live of 2 years";
        word2 = "The product has a shelf live of 3 years";
        assertThat(isOneEditApart(word1, word2), is(true));

        word1 = "Son of a gun!";
        word2 = "Son of a nun!";
        assertThat(isOneEditApart(word1, word2), is(true));

        word1 = "cat";
        word2 = "cats";
        assertThat(isOneEditApart(word1, word2), is(true));

        word1 = "cat";
        word2 = "cut";
        assertThat(isOneEditApart(word1, word2), is(true));

        word1 = "cat";
        word2 = "cast";
        assertThat(isOneEditApart(word1, word2), is(true));

        word1 = "cat";
        word2 = "at";
        assertThat(isOneEditApart(word1, word2), is(true));

        word1 = "cat";
        word2 = "acts";
        assertThat(isOneEditApart(word1, word2), is(false));

        word1 = "cat";
        word2 = "dog";
        assertThat(isOneEditApart(word1, word2), is(false));
    }

}
