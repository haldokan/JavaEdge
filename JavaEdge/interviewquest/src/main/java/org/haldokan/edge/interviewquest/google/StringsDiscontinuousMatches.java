package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - I actually return the indices of the matches which is much harder. Using
 * dynamic programming we build an index table that keeps track of a char index in the different developing matches. As
 * for complexity, for every char we examine the previous char matching indexes which leads to O(n^2) + m time where n is
 * the length of the first string and m is the length of the second string.
 *
 * The Question: 5_STAR
 *
 * Question: Given two strings, find number of discontinuous matches.
 * Example: “cat”, “catapult”
 * Output: 3   => “CATapult”, “CatApulT”, “CAtapulT”
 * <p>
 * Created by haytham.aldokanji on 5/26/16.
 */
public class StringsDiscontinuousMatches {

    public static void main(String[] args) {
        StringsDiscontinuousMatches driver = new StringsDiscontinuousMatches();

        driver.testBuildIndexTracker();
        driver.testGetCharIndexes();
        driver.testGetMatchIndexes();
        driver.testDiscontinuousMatches();
    }

    public Optional<String[]> discontinuousMatches(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() > str2.length()) {
            throw new IllegalArgumentException("Invalid input: " + str1 + "\n" + str2);
        }
        int[][] indexTracker = buildIndexTracker(str2);
        Optional<ListMultimap<Integer, int[]>> matchIndexesTable = getMatchIndexesTable(str1, indexTracker);

        if (!matchIndexesTable.isPresent()) {
            return Optional.empty();
        }

        List<int[]> matchIndexes = matchIndexesTable.get().get((int) str1.charAt(str1.length() - 1));
        String[] result = new String[matchIndexes.size()];

        for (int i = 0; i < matchIndexes.size(); i++) {
            int[] matchIndex = matchIndexes.get(i);
            StringBuilder match = new StringBuilder(str2);
            for (int index : matchIndex) {
                match.replace(index, index + 1, String.valueOf(match.charAt(index)).toUpperCase());
            }
            result[i] = match.toString();
        }
        return Optional.of(result);
    }

    private int[][] buildIndexTracker(String str2) {
        // going to assume reasonably the strings have 128-range ascii chars
        int[][] indexTracker = new int[128][str2.length() + 1];

        for (int i = 0; i < str2.length(); i++) {
            int[] charIndexes = indexTracker[str2.charAt(i)];
            charIndexes[0]++;
            charIndexes[charIndexes[0]] = i;
        }
        return indexTracker;
    }

    private Optional<ListMultimap<Integer, int[]>> getMatchIndexesTable(String str1, int[][] indexTracker) {
        ListMultimap<Integer, int[]> result = ArrayListMultimap.create();

        int firstChar = str1.charAt(0);
        Optional<int[]> potentialCharIndexes = getCharIndexes(firstChar, indexTracker);
        if (!potentialCharIndexes.isPresent()) {
            return Optional.empty();
        }

        int[] charIndexes = potentialCharIndexes.get();
        for (int charIndex : charIndexes) {
            result.put(firstChar, new int[]{charIndex});
        }

        int previousChar = firstChar;
        for (int i = 1; i < str1.length(); i++) {
            int currentChar = str1.charAt(i);
            potentialCharIndexes = getCharIndexes(currentChar, indexTracker);

            if (!potentialCharIndexes.isPresent()) {
                return Optional.empty();
            }

            charIndexes = potentialCharIndexes.get();
            for (int charIndex : charIndexes) {
                List<int[]> previousCharIndexes = result.get(previousChar);
                for (int[] previousCharIndex : previousCharIndexes) {
                    if (charIndex > previousCharIndex[previousCharIndex.length - 1]) {
                        int[] newCharIndex = new int[i + 1];
                        System.arraycopy(previousCharIndex, 0, newCharIndex, 0, previousCharIndex.length);
                        newCharIndex[i] = charIndex;
                        result.put(currentChar, newCharIndex);
                    }
                }
            }
            previousChar = currentChar;
        }
        return Optional.of(result);
    }

    private Optional<int[]> getCharIndexes(int chr, int[][] indexTracker) {
        int[] charIndexes = indexTracker[chr];
        if (charIndexes[0] == 0) {
            return Optional.empty();
        }
        return Optional.of(Arrays.copyOfRange(charIndexes, 1, charIndexes[0] + 1));
    }

    private void testBuildIndexTracker() {
        String str = "catapult";
        int[][] indexTracker = buildIndexTracker(str);

        assertThat(indexTracker['c'], is(new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0}));
        assertThat(indexTracker['a'], is(new int[]{2, 1, 3, 0, 0, 0, 0, 0, 0}));
        assertThat(indexTracker['t'], is(new int[]{2, 2, 7, 0, 0, 0, 0, 0, 0}));
        assertThat(indexTracker['p'], is(new int[]{1, 4, 0, 0, 0, 0, 0, 0, 0}));
        assertThat(indexTracker['u'], is(new int[]{1, 5, 0, 0, 0, 0, 0, 0, 0}));
        assertThat(indexTracker['l'], is(new int[]{1, 6, 0, 0, 0, 0, 0, 0, 0}));
    }

    private void testGetCharIndexes() {
        int[][] arr = new int[][]{
                {1, 0, 0, 0, 0, 0, 0, 0, 0},
                {2, 1, 3, 0, 0, 0, 0, 0, 0},
                {2, 2, 7, 0, 0, 0, 0, 0, 0}
        };

        assertThat(getCharIndexes(0, arr).get(), is(new int[]{0}));
        assertThat(getCharIndexes(1, arr).get(), is(new int[]{1, 3}));
        assertThat(getCharIndexes(2, arr).get(), is(new int[]{2, 7}));
    }

    private void testGetMatchIndexes() {
        String str1 = "catapult";
        int[][] indexTracker = buildIndexTracker(str1);

        String str2 = "cat";
        ListMultimap<Integer, int[]> matchIndexesTable = getMatchIndexesTable(str2, indexTracker).get();
        List<int[]> matchIndexes = matchIndexesTable.get((int) 't');
        assertThat(matchIndexes.get(0), is(new int[]{0, 1, 2}));
        assertThat(matchIndexes.get(1), is(new int[]{0, 1, 7}));
        assertThat(matchIndexes.get(2), is(new int[]{0, 3, 7}));
    }

    private void testDiscontinuousMatches() {
        String[] matches = discontinuousMatches("cat", "catapult").get();
        assertThat(matches.length, is(3));
        assertThat(Arrays.asList(matches), containsInAnyOrder("CATapult", "CAtapulT", "CatApulT"));

        matches = discontinuousMatches("cat", "catapult combat").get();
        assertThat(Arrays.asList(matches), containsInAnyOrder("CATapult combat", "CAtapulT combat", "CatApulT combat",
                "CAtapult combaT", "CatApult combaT", "Catapult combAT", "catapult CombAT"));

        matches = discontinuousMatches("cat", "ccaatt").get();
        assertThat(Arrays.asList(matches), containsInAnyOrder("CcAaTt", "cCAaTt", "CcaATt", "cCaATt", "CcAatT",
                "cCAatT", "CcaAtT", "cCaAtT"));

        matches = discontinuousMatches("c", "ccc").get();
        assertThat(Arrays.asList(matches), containsInAnyOrder("Ccc", "cCc", "ccC"));
    }
}
