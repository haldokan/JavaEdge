package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * My solution to a Google interview question - Used circular array to implement a sliding window for moving average
 * I think we can maintain a moving average value instead of averaging the array every time similar to what I did in the other implementation
 * The Question: 3.5_STAR
 *
 * Consider a setup where a program is continuously receiving floats as inputs (a stream of numbers).
 * Write a method that at any given time returns a moving average. That is the average of the last K numbers received.
 * If the method is called before the program has received K numbers, simply return the average of however many numbers
 * have been received thus far.
 * <p>
 * Created by haytham.aldokanji on 11/23/15.
 */
public class StreamMovingAverage2 {
    float[] movingAverage;
    int index;
    int count;

    public static void main(String[] args) {
        StreamMovingAverage2 driver = new StreamMovingAverage2(3);
        driver.onNum(5f);
        System.out.println(Arrays.toString(driver.movingAverage));
        System.out.printf("%d -> %f%n", driver.index, driver.average());

        driver.onNum(4f);
        System.out.println(Arrays.toString(driver.movingAverage));
        System.out.printf("%d -> %f%n", driver.index, driver.average());

        driver.onNum(3f);
        System.out.println(Arrays.toString(driver.movingAverage));
        System.out.printf("%d -> %f%n", driver.index, driver.average());

        driver.onNum(2f);
        System.out.println(Arrays.toString(driver.movingAverage));
        System.out.printf("%d -> %f%n", driver.index, driver.average());

        driver.onNum(1f);
        System.out.println(Arrays.toString(driver.movingAverage));
        System.out.printf("%d -> %f%n", driver.index, driver.average());

        driver.onNum(0.5f);
        System.out.println(Arrays.toString(driver.movingAverage));
        System.out.printf("%d -> %f%n", driver.index, driver.average());
    }

    public StreamMovingAverage2(int len) {
        this.movingAverage = new float[len + 1];
    }

    void onNum(float num) {
        movingAverage[index] = num;
        index = (index + 1) % movingAverage.length;
        movingAverage[index] = 0;
        count++;
    }

    float average() {
        float avg = 0f;
        for (float v : movingAverage) {
            avg += v;
        }
        return avg / Math.min(count, (movingAverage.length - 1));
    }
}
