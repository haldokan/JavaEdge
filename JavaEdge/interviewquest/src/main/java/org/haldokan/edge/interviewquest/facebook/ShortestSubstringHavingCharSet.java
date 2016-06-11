package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question
 * The Question: 4_STAR
 * You are given a set of unique characters and a string.
 * <p>
 * Find the smallest substring of the string containing all the characters in the set.
 * <p>
 * ex:
 * Set : [a, b, c]
 * String : "abbcbcba"
 * <p>
 * Result: "cba"
 * Created by haytham.aldokanji on 5/14/16.
 */
public class ShortestSubstringHavingCharSet {

    public static void main(String[] args) {
        ShortestSubstringHavingCharSet driver = new ShortestSubstringHavingCharSet();
        driver.testGetIndexes();
        driver.testShortestSubstring();
    }

    public Optional<String> shortestSubstring(String string, Set<Character> charset) {
        char[] stringChars = string.toCharArray();
        Optional<int[]> substringIndexes = Optional.empty();

        for (int i = 0; i < stringChars.length; i++) {
            char currentChar = stringChars[i];

            if (charset.contains(currentChar)) {
                Optional<int[]> indexes = getIndexes(stringChars, i, charset);
                substringIndexes = updateIndexes(substringIndexes, indexes);
            }
        }
        if (substringIndexes.isPresent()) {
            return Optional.of(String.valueOf(stringChars,
                    substringIndexes.get()[0],
                    substringIndexes.get()[1] - substringIndexes.get()[0] + 1));
        }
        return Optional.empty();
    }

    private Optional<int[]> getIndexes(char[] stringChars, int index, Set<Character> charset) {
        if (index < charset.size()) {
            return Optional.empty();
        }

        Set<Character> found = new HashSet<>();
        int i;
        for (i = index; i >= 0 && found.size() != charset.size(); i--) {
            char currentChar = stringChars[i];
            if (charset.contains(currentChar) && !found.contains(currentChar)) {
                found.add(currentChar);
            }
        }
        if (found.size() == charset.size()) {
            return Optional.of(new int[]{i + 1, index});
        }
        return Optional.empty();
    }

    private Optional<int[]> updateIndexes(Optional<int[]> currentIndexes, Optional<int[]> newerIndexes) {
        if (!currentIndexes.isPresent()) {
            return newerIndexes;
        }
        int[] current = currentIndexes.get();
        int[] newer = newerIndexes.get();

        return newer[1] - newer[0] < current[1] - current[0] ? newerIndexes : currentIndexes;

    }

    private void testGetIndexes() {
        Set<Character> charset = new HashSet<>(Arrays.asList('a', 'b', 'c'));
        char[] stringChars = "abbcbcba".toCharArray();
        int index = 0;
        assertThat(getIndexes(stringChars, index, charset).isPresent(), is(false));
        index++;
        assertThat(getIndexes(stringChars, index, charset).isPresent(), is(false));
        index++;
        assertThat(getIndexes(stringChars, index, charset).isPresent(), is(false));
        index++;
        assertThat(getIndexes(stringChars, index, charset).get(), is(new int[]{0, 3}));
        index++;
        assertThat(getIndexes(stringChars, index, charset).get(), is(new int[]{0, 4}));
        index++;
        assertThat(getIndexes(stringChars, index, charset).get(), is(new int[]{0, 5}));
        index++;
        assertThat(getIndexes(stringChars, index, charset).get(), is(new int[]{0, 6}));
        index++;
        assertThat(getIndexes(stringChars, index, charset).get(), is(new int[]{5, 7}));
    }

    private void testShortestSubstring() {
        Set<Character> charset = new HashSet<>(Arrays.asList('a', 'b', 'c'));

        String string = "abbcbcba";
        String shortest = shortestSubstring(string, charset).get();
        System.out.println(shortest);
        assertThat(shortest, is("cba"));

        string = "abbxyzcbcbzza1234343343433bzac";
        shortest = shortestSubstring(string, charset).get();
        System.out.println(shortest);
        assertThat(shortest, is(string.substring(string.length() - 4, string.length())));

        string = "axxbcaxb";
        shortest = shortestSubstring(string, charset).get();
        System.out.println(shortest);
        assertThat(shortest, is("bca"));
    }
}
