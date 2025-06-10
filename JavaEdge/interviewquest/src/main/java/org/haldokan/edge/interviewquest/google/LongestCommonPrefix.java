package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.Lists;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question O(1) space and O(m x n) time where m is the length of a phrase
 * and n is the number of phrases
 * The problem can be solved using prefix trie in O(m + n) but that is very inefficient in space: insert every phrase
 * in the prefix tries keeping track of the length longest prefix path in the trie.
 *
 * The Question: 3_STAR
 *
 * Find the longest common prefix in a list of phrases.
 * For instance; "i love all dogs", "i love cats" should return "i love".
 * <p>
 * Created by haytham.aldokanji on 5/20/16.
 */
public class LongestCommonPrefix {

    public static void main(String[] args) {
        LongestCommonPrefix driver = new LongestCommonPrefix();

        List<String> phrases = Lists.newArrayList("i love all dogs", "i love cats");
        String longestPrefix = driver.longestCommonPrefix(phrases);
        assertThat(longestPrefix, is("i love "));

        phrases = Lists.newArrayList("i love all dogs", "i love all cats");
        longestPrefix = driver.longestCommonPrefix(phrases);
        assertThat(longestPrefix, is("i love all "));

        phrases = Lists.newArrayList("i love all cats and dogs", "i love all cats");
        longestPrefix = driver.longestCommonPrefix(phrases);
        assertThat(longestPrefix, is("i love all cats"));

        phrases = Lists.newArrayList("i love all cats and dogs", "i love all cats and", "i love all cats",
                "i love all", "i love");
        longestPrefix = driver.longestCommonPrefix(phrases);
        assertThat(longestPrefix, is("i love"));
    }

    public String longestCommonPrefix(List<String> phrases) {
        if (phrases == null || phrases.isEmpty()) {
            throw new IllegalStateException("Invalid input: " + phrases);
        }
        if (phrases.size() == 1) {
            return phrases.get(0);
        }

        int smallestPhraseIndex = 0;
        int index;
        for (index = 0; index < phrases.size(); index++) {
            if (phrases.get(index).length() < phrases.get(smallestPhraseIndex).length()) {
                smallestPhraseIndex = index;
            }
        }
        String smallestPhrase = phrases.get(smallestPhraseIndex);

        int longest;
        for (longest = 0; longest < smallestPhrase.length(); longest++) {
            char currentChar = smallestPhrase.charAt(longest);
            int phraseIndex;
            for (phraseIndex = 0; phraseIndex < phrases.size(); phraseIndex++) {
                String currentPhrase = phrases.get(phraseIndex);
                if (currentChar != currentPhrase.charAt(longest)) {
                    break;
                }
            }
            if (phraseIndex < phrases.size()) {
                break;
            }
        }
        return smallestPhrase.substring(0, longest);
    }
}
