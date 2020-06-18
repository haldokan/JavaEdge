package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview questions
 * The Question: 3_STAR
 * <p>
 * Sort the input character array based on the dictionary given.
 * <p>
 * For eg:, If input word is “SHEEP“, sorting will make it as “EEHPS“.
 * <p>
 * But in the dictionary, E may not be at first. As per the dictionary, if P is first, S followed and E later and finally H.
 * <p>
 * Then sorted array is “PSEEH“.
 * Created by haytham.aldokanji on 7/3/16.
 */
public class SortBasedOnCustomOrderedAlphabet {

    public static void main(String[] args) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        char[] reversedAlphabet = new char[alphabet.length()];
        int j = 0;
        for (int i = alphabet.length() - 1; i >= 0; i--) {
            reversedAlphabet[j++] = alphabet.charAt(i);
        }

        SortBasedOnCustomOrderedAlphabet driver = new SortBasedOnCustomOrderedAlphabet();
        Character[] charSequence = new Character[]{'h', 'e', 'l', 'l', 'o'};

        driver.sort(charSequence, reversedAlphabet);
        assertThat(charSequence, is(new Character[]{'o', 'l', 'l', 'h', 'e'}));
    }

    public void sort(Character[] input, char[] alphabet) {
        int[] ascii = new int[128];
        IntStream.range(0, alphabet.length).forEach(index -> ascii[alphabet[index]] = index);
        // we cannot sort char[] with a comparator; it has to be Character[]; One of Java's sucki aspects
        Arrays.sort(input, Comparator.comparingInt(v -> ascii[v]));
    }
}
