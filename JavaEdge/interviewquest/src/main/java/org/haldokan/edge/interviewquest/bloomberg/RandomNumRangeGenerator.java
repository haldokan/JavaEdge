package org.haldokan.edge.interviewquest.bloomberg;

import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * My solution to a Bloomberg inteview question
 * <p>
 * The question 3_STAR
 * <p>
 * You have a function f1() that generates 0 or 1 with the equal probability. Write a function f29() that generates a
 * number between 0 and 29 with equal probability.
 * <p>
 * Created by haytham.aldokanji on 8/6/16.
 */
public class RandomNumRangeGenerator {
    private final Supplier<Double> randomNumGenerator;
    private final double probabilityUnit;

    public RandomNumRangeGenerator(int rangeCeiling, Supplier<Double> randomNumGenerator) {
        // assuming the supplied random function generator generates values b/w 0 and 1
        this.probabilityUnit = 1d / rangeCeiling;
        this.randomNumGenerator = randomNumGenerator;
    }

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        RandomNumRangeGenerator rangeGenerator = new RandomNumRangeGenerator(29, Math::random);
        IntStream.range(0, 10).forEach(i -> System.out.printf("%f%n", rangeGenerator.randomNumGenerator.get()));
        // our derived generator
        IntStream.range(0, 10).forEach(i -> System.out.printf("%d%n", rangeGenerator.nextInt()));
        IntStream.range(0, 10).forEach(i -> System.out.printf("%f%n", rangeGenerator.nextDouble()));
    }

    public int nextInt() {
        return (int) Math.round(randomNumGenerator.get() / probabilityUnit);
    }

    public double nextDouble() {
        return randomNumGenerator.get() / probabilityUnit;
    }
}
