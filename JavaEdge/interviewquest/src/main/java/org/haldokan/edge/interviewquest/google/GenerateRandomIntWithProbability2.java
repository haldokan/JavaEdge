package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;
import java.util.Random;

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
 * w / sum = 1/9, 2/9, 3/9, 2/9, 1/9
 * <p>
 * Example results:
 * n = 0, 1, 2, 3, 4
 * <p>
 * Created by haytham.aldokanji on 7/3/16.
 */
public class GenerateRandomIntWithProbability2 {
    int[] probabilities;
    Random random = new Random();

    public static void main(String[] args) {
        GenerateRandomIntWithProbability2 driver = new GenerateRandomIntWithProbability2(new int[]{5, 2, 9, 3, 1});
        for (int i = 0; i < 100; i++) {
            System.out.printf("%d ", driver.weightedRandom());
        }
        System.out.println();
    }

    public GenerateRandomIntWithProbability2(int[] distribution) {
        this.probabilities = probabilities(distribution);
    }

    int weightedRandom() {
        return probabilities[Math.abs(random.nextInt()) % probabilities.length];
    }

    int[] probabilities(int[] distribution) {
        int total = Arrays.stream(distribution).sum();
        int[] probs = new int[total];

        int k = 0;
        for (int i = 0; i < distribution.length; i++) {
            for (int j = 0; j < distribution[i]; j++) {
                probs[k++] = i;
            }
        }
        return probs;
    }
}
