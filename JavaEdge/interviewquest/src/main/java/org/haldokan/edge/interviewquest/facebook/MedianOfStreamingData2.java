package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.TreeMap;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Facebook interview question - used a combination of min & max heaps to maintain the median at the roots
 * of the 2 heaps as explained in this algorithm: https://www.geeksforgeeks.org/median-of-stream-of-integers-running-integers/
 * Such algorithms are call online-algorithms: the value they calculate depends on the last value in a data stream
 * <p
 * The Question: 5_STAR
 * Create the data structure for a component that will receive a series of numbers over time and, when asked,
 * returns the median of all received elements.
 * <p>
 * (Median: the numerical value separating the higher half of a data sample from the lower half. Example: if the series is
 * <p>
 * 2, 7, 4, 9, 1, 5, 8, 3, 6
 * then the median is 5
 * <p>
 * Model the data structure for a component that would have these two methods:
 *
 * @interface SampleHandler {
 * - (void)addNumber:(NSNumber*)number;
 * - (NSNumber*)median;
 * }
 * Justify your decisions. Calculate the complexity of each method.
 * <p>
 * Created by haytham.aldokanji on 6/26/20.
 */
public class MedianOfStreamingData2 {
    Float median = null;
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.comparingInt(val -> -val));
    PriorityQueue<Integer> minHeap = new PriorityQueue<>(Comparator.comparingInt(val -> val));

    public static void main(String[] args) {
        MedianOfStreamingData2 driver = new MedianOfStreamingData2();

        driver.addNum(0);
        System.out.println(driver.median);

        driver.addNum(7);
        System.out.println(driver.median);

        driver.addNum(3);
        System.out.println(driver.median);

        driver.addNum(9);
        System.out.println(driver.median);

        driver.addNum(2);
        System.out.println(driver.median);

        driver.addNum(4);
        System.out.println(driver.median);

        driver.addNum(11);
        System.out.println(driver.median);

        driver.addNum(17);
        System.out.println(driver.median);

        driver.addNum(8);
        System.out.println(driver.median);
    }

    void addNum(int num) {
        if (median == null) {
            maxHeap.add(num);
            median = (float) num;
        } else if (maxHeap.size() == minHeap.size()) {
            if (num < median) {
                maxHeap.add(num);
                median = (float) maxHeap.peek();
            } else {
                minHeap.add(num);
                median = (float) minHeap.peek();
            }
        } else if (maxHeap.size() > minHeap.size()) {
            if (num < median) {
                minHeap.add(maxHeap.remove());
                maxHeap.add(num);
            } else {
                minHeap.add(num);
            }
            median = ((float) (maxHeap.peek() + minHeap.peek())) / 2;
        } else if (maxHeap.size() < minHeap.size()) {
            if (num <= median) {
                maxHeap.add(num);
            } else {
                maxHeap.add(minHeap.remove());
                minHeap.add(num);
            }
            median = ((float) (maxHeap.peek() + minHeap.peek())) / 2;
        }
    }

    float median() {
        return median;
    }
}