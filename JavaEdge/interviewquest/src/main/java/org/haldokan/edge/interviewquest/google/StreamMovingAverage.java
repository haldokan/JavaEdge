package org.haldokan.edge.interviewquest.google;

import java.util.*;
import java.util.stream.Stream;

/**
 * My solution to a Google interview question
 * <p>
 * Consider a setup where a program is continuously receiving floats as inputs (a stream of numbers).
 * Write a method that at any given time returns a moving average. That is the average of the last K numbers received.
 * If the method is called before the program has received K numbers, simply return the average of however many numbers
 * have been received thus far.
 * <p>
 * Created by haytham.aldokanji on 11/23/15.
 */
public class StreamMovingAverage {
    private final Deque<Float> sample;
    private float movingAverage;
    private int samplingSize;

    public StreamMovingAverage(int samplingSize) {
        this.samplingSize = samplingSize;
        this.sample = new LinkedList<>();
    }

    public static void main(String[] args) {
        List<Float> nums = new ArrayList<>(Arrays.asList(6f, 3f, 6f, 9f, 12f, 24f, 30f, 7f, 33f));
        StreamMovingAverage driver = new StreamMovingAverage(3);
        driver.steamIn(nums.stream());
    }

    public void steamIn(Stream<Float> stream) {
        stream.forEach(f -> {
            if (samplingSize == sample.size()) {
                movingAverage -= sample.removeFirst() / samplingSize;
                movingAverage += f / samplingSize;
                sample.add(f);
                System.out.println(">>" + movingAverage);
            } else {
                movingAverage *= sample.size();
                sample.add(f);
                movingAverage = (movingAverage + f) / sample.size();
                System.out.println(">" + movingAverage);
            }
        });
    }

    public float getMovingAverage() {
        return movingAverage;
    }
}
