package org.haldokan.edge.interviewquest.amazon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * My solution to an Amazon interview question
 * The Question: 3_STAR
 * given a string calculate the frequency of characters, output the array with the letter and frequency.
 * (such as: for “abbcdc”, the output should be (a,1),(b,2),(c,2),(d,1))
 */
public class CharFrequencyInString {

    public static void main(String[] args) {
        System.out.println(charFreq("aababcca"));
    }

    private static List<CharFreq> charFreq(String input) {
        Map<Character, Integer> freqByChar = new HashMap<>();

        for (int i = 0; i < input.length(); i++) {
            freqByChar.compute(input.charAt(i), (k, v) -> (v == null ? 1 : ++v));
        }
        return freqByChar.entrySet()
                .stream()
                .map(e -> new CharFreq(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private static class CharFreq {
        private final Character chr;
        private final Integer freq;

        public CharFreq(Character chr, Integer freq) {
            this.chr = chr;
            this.freq = freq;
        }

        @Override
        public String toString() {
            return "(" + chr + ", " + freq + ")";
        }
    }
}
