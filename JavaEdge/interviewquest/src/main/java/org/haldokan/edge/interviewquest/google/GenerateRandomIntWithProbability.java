package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question
 * The Question: 4_STAR
 * <p>
 * Design and implement a class to generate random numbers in an arbitrary probability distribution given by an array
 * of integer weights, i.e. for int[] w return a number, n, from 0 to w.length - 1 with probability w[n] / sum(w).
 * Using an existing random number generator with a uniform distribution is permitted.
 * <p>
 * Example distribution:
 * w = 1, 2, 3, 2, 1
 * <p>
 * Example probabilities:
 * w / sum = 1/9, 2/9, 1/3, 2/9, 1/9
 * <p>
 * Example results:
 * n = 0, 1, 2, 3, 4
 * <p>
 * Created by haytham.aldokanji on 7/3/16.
 */
public class GenerateRandomIntWithProbability {
    private ProbabilityRange[] probabilityRanges;

    public GenerateRandomIntWithProbability(int[] weights) {
        buildGenerator(weights);
    }

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        int[] weights = new int[]{7, 1, 1, 1, 12, 1, 2, 1, 10};
        GenerateRandomIntWithProbability generator = new GenerateRandomIntWithProbability(weights);

        // we expect more 4, 8, 0, 6 (in this order)
        Map<Integer, Integer> frequencyByRandNum = new HashMap<>();
        IntStream.range(0, 10_000).forEach(i -> {
            int rand = generator.random();
            frequencyByRandNum.compute(rand, (k, v) -> v == null ? 1 : v + 1);
        });

        System.out.printf("%s%n", frequencyByRandNum);
        Integer[] frequencies = frequencyByRandNum.values().toArray(new Integer[frequencyByRandNum.size()]);
        Arrays.sort(frequencies, (v1, v2) -> v2 - v1);
        System.out.printf("%s%n", Arrays.toString(frequencies));

        assertThat(frequencies[0], is(frequencyByRandNum.get(4)));
        assertThat(frequencies[1], is(frequencyByRandNum.get(8)));
        assertThat(frequencies[2], is(frequencyByRandNum.get(0)));
        assertThat(frequencies[3], is(frequencyByRandNum.get(6)));
    }

    public int random() {
        double rand = Math.random();
        int randomInteger = 0;
        for (ProbabilityRange range : probabilityRanges) {
            if (range.inRange(rand)) {
                break;
            }
            randomInteger++;
        }
        return randomInteger;
    }

    public void buildGenerator(int[] weights) {
        probabilityRanges = new ProbabilityRange[weights.length];
        double rangeStart = 0d;
        double sampleWeight = Arrays.stream(weights).sum();

        for (int i = 0; i < weights.length; i++) {
            double probability = (double) weights[i] / sampleWeight;
            probabilityRanges[i] = new ProbabilityRange(rangeStart, rangeStart + probability);
            rangeStart += probability;
        }
    }

    private static class ProbabilityRange {
        private final double start, end;

        public ProbabilityRange(double start, double end) {
            this.start = start;
            this.end = end;
        }

        public boolean inRange(double random) {
            return random >= start && random < end;
        }

        @Override
        public String toString() {
            return "(" + start + ", " + end + ")";
        }
    }
}
