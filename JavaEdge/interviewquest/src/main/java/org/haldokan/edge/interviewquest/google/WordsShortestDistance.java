package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - I solved for the general case of N words. In this solution I used another
 * solution that I provided to a different Google interview question: it does an N-way merge of sorted arrays.
 *
 * NOTE: check the other solution I provide in WordsShortestDistance2 which I think fixes a possible issue in this one
 * The Question (4_STARS):
 *
 * We have words and there positions in a paragraph in sorted order. Write an algorithm to find the least distance
 * for a given 3 words.
 * eg. for 3 words
 * job: 5, 9, 17
 * in: 4, 13, 18
 * google: 8, 19, 21
 * ...
 * ...
 * Answer: 17, 18, 19
 * Can you extend it to "n" words?
 * <p>
 * Context: In Google search results, the search terms are highlighted in the short paragraph that shows up. We need to
 * find the shortest sentence that has all the words if we have word positions as mentioned above.
 * <p>
 * Created by haytham.aldokanji on 5/31/16.
 */
public class WordsShortestDistance {

    public static void main(String[] args) {
        WordsShortestDistance driver = new WordsShortestDistance();
        driver.test1();
        driver.test2();
        driver.test3();
    }

    private Integer[] shortestWordSetPositions(WordOccurrence[][] wordOccurrences) {
        // using my solution to N-way sorted array merge (another Google interview question)
        GenerifiedNWaySortedArrayMerge<WordOccurrence> arrayMerger = new GenerifiedNWaySortedArrayMerge<>();
        WordOccurrence[] mergedOccurrences = arrayMerger.merge(wordOccurrences, WordOccurrence.class);

        int numWords = wordOccurrences.length;
        // we have an extra slot (the 1st) to use for holding the sum of positions distances
        Integer[] shortestWordSetPositions = new Integer[numWords + 1];

        for (int i = 0; i < mergedOccurrences.length; i++) {
            int j = i + numWords;
            if (j <= mergedOccurrences.length) {
                WordOccurrence[] potentialPositionSet = new WordOccurrence[numWords];

                int p = 0;
                for (int k = i; k < j; k++) {
                    potentialPositionSet[p++] = mergedOccurrences[k];
                }
                updateShortestPositions(shortestWordSetPositions, potentialPositionSet);
            }
        }
        return shortestWordSetPositions;
    }

    private void updateShortestPositions(Integer[] shortestYet, WordOccurrence[] newPositionSet) {
        // the new position set must include all words to be a valid candidate
        if (Arrays.stream(newPositionSet).distinct().count() == newPositionSet.length) {
            int distanceSum = 0;
            for (int i = 0; i < newPositionSet.length - 1; i++) {
                distanceSum += newPositionSet[i + 1].position - newPositionSet[i].position;
            }
            if (shortestYet[0] == null || distanceSum < shortestYet[0]) {
                shortestYet[0] = distanceSum;
                Integer[] positions = Arrays.stream(newPositionSet).map(v -> v.position).toArray(Integer[]::new);
                System.arraycopy(positions, 0, shortestYet, 1, newPositionSet.length);
            }
        }
    }

    private void test1() {
        WordOccurrence[][] wordOccurrences = new WordOccurrence[][]{
                {new WordOccurrence("Job", 5), new WordOccurrence("Job", 9), new WordOccurrence("Job", 17)},
                {new WordOccurrence("in", 4), new WordOccurrence("in", 13), new WordOccurrence("in", 18), new WordOccurrence("in", 22)},
                {new WordOccurrence("Google", 8), new WordOccurrence("Google", 19), new WordOccurrence("Google", 21)}
        };
        // first value is the distance
        assertThat(shortestWordSetPositions(wordOccurrences), is(new Integer[]{2, 17, 18, 19}));
    }

    private void test2() {
        WordOccurrence[][] wordOccurrences = new WordOccurrence[][]{
                {new WordOccurrence("Job", 5), new WordOccurrence("Job", 9), new WordOccurrence("Job", 20)},
                {new WordOccurrence("in", 4), new WordOccurrence("in", 13), new WordOccurrence("in", 17), new WordOccurrence("in", 22)},
                {new WordOccurrence("Google", 8), new WordOccurrence("Google", 19), new WordOccurrence("Google", 21)}
        };
        // first value is the distance
        assertThat(shortestWordSetPositions(wordOccurrences), is(new Integer[]{2, 20, 21, 22}));
    }

    private void test3() {
        WordOccurrence[][] wordOccurrences = new WordOccurrence[][]{
                {new WordOccurrence("Job", 5), new WordOccurrence("Job", 9), new WordOccurrence("Job", 20)},
                {new WordOccurrence("in", 4), new WordOccurrence("in", 13), new WordOccurrence("in", 15), new WordOccurrence("in", 25)},
                {new WordOccurrence("Google", 2), new WordOccurrence("Google", 19), new WordOccurrence("Google", 21)}
        };
        // first value is the distance
        assertThat(shortestWordSetPositions(wordOccurrences), is(new Integer[]{3, 2, 4, 5}));
    }

    /**
     * Note: this class has a natural ordering that is inconsistent with equals.
     */
    private static class WordOccurrence implements Comparable<WordOccurrence> {
        private final String word;
        private final int position;

        public WordOccurrence(String word, int position) {
            this.word = word;
            this.position = position;
        }

        @Override
        public int compareTo(WordOccurrence other) {
            return position - other.position;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WordOccurrence that = (WordOccurrence) o;

            return !(word != null ? !word.equals(that.word) : that.word != null);

        }

        @Override
        public int hashCode() {
            return word != null ? word.hashCode() : 0;
        }

        @Override
        public String toString() {
            return word + "@" + position;
        }
    }
}
