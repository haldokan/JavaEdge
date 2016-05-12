package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - O(n + m) where n is the number of words, and m is the length of a word
 * <p>
 * Given a list of strings, return a list of lists of strings that groups all anagrams.
 * <p>
 * Ex. given {trees, bike, cars, steer, arcs}
 * return { {cars, arcs}, {bike}, {trees, steer} }
 * <p>
 * Created by haytham.aldokanji on 5/11/16.
 */
public class FindingPalindromes {
    public static void main(String[] args) {
        FindingPalindromes driver = new FindingPalindromes();

        driver.test1();
        driver.test2();
    }

    public List<List<String>> findPalindrome(String[] words) {
        List<List<String>> palindromes = new ArrayList<>();
        boolean[] excludedWords = new boolean[words.length];

        for (int i = 0; i < words.length; i++) {
            if (excludedWords[i]) {
                //don't much like 'continue' but it seem appropriate here
                continue;
            }
            List<String> wordPalindromes = new ArrayList<>();
            String word1 = words[i];

            wordPalindromes.add(word1);
            excludedWords[i] = true;

            for (int j = i + 1; j < words.length; j++) {
                String word2 = words[j];
                if (arePalindrome(word1, word2)) {
                    wordPalindromes.add(word2);
                    excludedWords[j] = true;
                }
            }
            palindromes.add(wordPalindromes);
        }
        return palindromes;
    }

    // O(m + n)
    private boolean arePalindrome(String word1, String word2) {
        if (word1.length() != word2.length()) {
            return false;
        }

        int asciiWord1 = 0;
        for (char chr : word1.toCharArray()) {
            asciiWord1 += chr;
        }

        int asciiWord2 = 0;
        for (char chr : word2.toCharArray()) {
            asciiWord2 += chr;
        }

        return asciiWord1 == asciiWord2;
    }

    private void test1() {
        assertThat(arePalindrome("trees", "steer"), is(true));
        assertThat(arePalindrome("trees", "steex"), is(false));
    }

    private void test2() {
        String[] words = new String[]{"trees", "cars", "bike", "steer", "arcs"};
        List<List<String>> palindromes = findPalindrome(words);
        System.out.println(palindromes);

        assertThat(palindromes.size(), is(3));
        assertThat(palindromes, containsInAnyOrder(equalTo(Arrays.asList("trees", "steer")),
                equalTo(Arrays.asList("cars", "arcs")), equalTo(Arrays.asList("bike"))));
    }
}
