package org.haldokan.edge.interviewquest.google;

import org.haldokan.edge.interviewquest.google.GenerifiedNWaySortedArrayMerge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * * We have words and there positions in a paragraph in sorted order. Write an algorithm to find the least distance
 * * for a given 3 words.
 * * eg. for 3 words
 * * job: 5, 9, 17
 * * in: 4, 13, 18
 * * google: 8, 19, 21
 * * ...
 * * ...
 * * Answer: 17, 18, 19
 * * Can you extend it to "n" words?
 * * <p>
 * * Context: In Google search results, the search terms are highlighted in the short paragraph that shows up. We need to
 * * find the shortest sentence that has all the words if we have word positions as mentioned above.
 * * <p>
 */
public class WordsShortestDistance2 {
    static WordIndex[][] wordIndexTable = new WordIndex[][] {
        new WordIndex[] {new WordIndex("job", 5), new WordIndex("job", 9), new WordIndex("job", 17)},
        new WordIndex[] {new WordIndex("in", 4), new WordIndex("in", 13), new WordIndex("in", 18) },
        new WordIndex[] {new WordIndex("google", 8), new WordIndex("google", 19), new WordIndex("google", 21),}
    };

    public static void main(String[] args) {
        WordsShortestDistance2 driver = new WordsShortestDistance2();
        driver.test1();
        driver.test2();
    }

    void test1() {
        GenerifiedNWaySortedArrayMerge<WordIndex> arrayMerger = new GenerifiedNWaySortedArrayMerge<>();
        WordIndex[] merged = arrayMerger.merge(wordIndexTable, WordIndex.class);

        System.out.println(Arrays.toString(currentClosestPositions(merged, 0, 3)));
        System.out.println(Arrays.toString(currentClosestPositions(merged, 1, 3)));
        System.out.println(Arrays.toString(currentClosestPositions(merged, 3, 3)));
        System.out.println(Arrays.toString(currentClosestPositions(merged, 4, 3)));
        System.out.println(Arrays.toString(currentClosestPositions(merged, 5, 3)));
        System.out.println(Arrays.toString(currentClosestPositions(merged, 6, 3)));
    }

    void test2() {
        System.out.println(Arrays.toString(closestPositions(wordIndexTable)));
    }

    WordIndex[] currentClosestPositions(WordIndex[] merged, int start, int numWords) {
        Set<String> foundWords = new HashSet<>();
        List<WordIndex> foundWordIndexes = new ArrayList<>();

        for (int i = start; i < merged.length; i++) {
            if (foundWords.size() == numWords) {
                break;
            }
            if (foundWords.add(merged[i].word)) {
                foundWordIndexes.add(merged[i]);
            }
        }
        return foundWordIndexes.toArray(new WordIndex[]{});
    }

    WordIndex[] closestPositions(WordIndex[][] wordIndexes) {
        int numWords = wordIndexes.length;
        int startIndex = 0;

        GenerifiedNWaySortedArrayMerge<WordIndex> arrayMerger = new GenerifiedNWaySortedArrayMerge<>();
        WordIndex[] merged = arrayMerger.merge(wordIndexes, WordIndex.class);

        WordIndex[] foundIndexes = currentClosestPositions(merged, startIndex, numWords);
        WordIndex[] closest = null;
        int shortest = 0;

        while (foundIndexes.length == numWords) {
            int distance = 0;
            for (int i = 0; i < foundIndexes.length - 1; i++) {
                distance += foundIndexes[i+1].index - foundIndexes[i].index;
            }
            if (shortest == 0 || distance < shortest) {
                shortest = distance;
                closest = foundIndexes;
            }
            foundIndexes = currentClosestPositions(merged, startIndex++, numWords);
        }
        return closest;
    }

    static class WordIndex implements Comparable<WordIndex> {
        String word;
        int index;

        public WordIndex(String word, int index) {
            this.word = word;
            this.index = index;
        }

        @Override
        public String toString() {
            return String.format("(%s, %d)", word, index);
        }

        @Override
        public int compareTo(WordIndex o) {
            return index - o.index;
        }
    }
}
