package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * My solution to an Amazon interview question - the core of the problem is to merge n-sorted-arrays which is what I implemented w/o
 * worrying too much about the stream aspect. I assumed that the streams are written to growing array lists and the merging is done into
 * a result stream that grows as the input streams keep on running.
 *
 * The Question: 4-STAR
 * Given multiple input streams of sorted numbers of infinite size, produce a single sorted output stream.
 *
 * (The size of all the input streams are unknown)
 *
 * For eg.
 *
 * Input Stream 1: 2,4,5,6,7,8...........
 * Input Stream 2: 1,3,9,12..............
 * Input Stream 3: 10,11,13,14........
 *
 * Output Stream:
 * 1,2,3,4,5,6,7,8,9,10,11,12,13...............
 *
 * 07/20/20
 */
public class SortMultipleSortedInputStreams {
    public static void main(String[] args) {
        List<List<Integer>> streams = new ArrayList<>();
        List<Integer> stream1 = Arrays.asList(1, 3, 5, 7, 9, 11, 15);
        List<Integer> stream2 = Arrays.asList(0, 2, 4, 6, 8, 10, 12, 14);

        streams.add(stream1);
        streams.add(stream2);

        System.out.println(merge(streams));
    }

    static List<Integer> merge(List<List<Integer>> streams) {
        List<Integer> merged = new ArrayList<>();
        int[] indexes = new int[streams.size()];

        for (;;) {
            Integer val = null;
            int valIndex = 0;
            System.out.println(merged);
            for (int i = 0; i < streams.size(); i++) {
                List<Integer> currentStream = streams.get(i);
                // assuming the array list that backs the stream will always have a new val (in reality we have to account for slows streams)
                int currentVal = currentStream.get(indexes[i]);
                if (val == null || currentVal < val) {
                    val  = currentVal;
                    valIndex = i;
                }
            }
            merged.add(val);
            indexes[valIndex] += 1;
        }
    }
}
