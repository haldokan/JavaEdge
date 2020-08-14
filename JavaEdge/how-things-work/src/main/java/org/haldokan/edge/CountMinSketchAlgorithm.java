package org.haldokan.edge;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.*;

/**
 * My implementation of the Algorithm Count-Min-Sketch. Tested with some real data sizes of youtube video views.
 * I chose delta = 0.05 which yields 3 hash functions (rows),
 * and epsilon = 0.0000027 which yields 1_006_771 rows. The test runs using a number of iterations equal to the number of youtube
 * views per minute: 347_256_947 and assuming 1000_000 unique videos per minute.
 *
 * In the test I provide, the printed heap of the top 1000 videos shows 133 that are not among the 10_000 that have
 * skewed probability of 10% (this is attributed to hash collisions).
 *
 * The implementation is based on the data structure description found here: https://en.wikipedia.org/wiki/Count-min_sketch
 */

public class CountMinSketchAlgorithm {
    private static final HashFunction[] HASH_FUNCS = new HashFunction[]{
            Hashing.sha512(),
            Hashing.sha384(),
            Hashing.sha256(),
            Hashing.murmur3_128(),
            Hashing.murmur3_32(),
            Hashing.crc32(),
            Hashing.crc32c(),
            Hashing.adler32(),
    };

    private final int[][] sketch;
    private final SketchParams sketchParams;

    public static void main(String[] args) {
        test();
    }

    static CountMinSketchAlgorithm create(SketchParams sketchParams) {
        return new CountMinSketchAlgorithm(sketchParams);
    }

    private CountMinSketchAlgorithm(SketchParams sketchParams) {
        System.out.printf("sketch params: %s%n", sketchParams);
        this.sketchParams = sketchParams;
        if (this.sketchParams.getNumRows() > HASH_FUNCS.length) {
            throw new IllegalArgumentException(String.format("cannot support this level of probability - probability: %f, numRows: %d%n", this.sketchParams.getProbability(), this.sketchParams.getNumRows()));
        }
        sketch = new int[this.sketchParams.getNumRows()][this.sketchParams.getNumCols()];
    }

    private int hashDataValue(String dataValue, HashFunction hashFunction) {
        return Math.abs(hashFunction.hashString(dataValue, Charset.defaultCharset()).asInt() % sketchParams.getNumCols());
    }
    public void onDataValue(String value) {
        for (int i = 0; i < sketchParams.getNumRows(); i++) {
            int column = hashDataValue(value, HASH_FUNCS[i]);
            sketch[i][column] += 1;
        }
    }

    public int dataValueFrequency(String value) {
        int frequency = 0;
        for (int i = 0; i < sketchParams.getNumRows(); i++) {
            int column = hashDataValue(value, HASH_FUNCS[i]);;
            int currentFrequency = sketch[i][column];
            if (frequency == 0 || currentFrequency < frequency) {
                frequency = currentFrequency;
            }
        }
        return frequency;
    }

    private static final class SketchParams {
        private static final double EULER_NUM = 2.71828d;

        double delta;
        double epsilon;
        // these default values yield d = 3 hash functions (rows), and w = 1_006_771 columns
        private static final SketchParams DEFAULT = new SketchParams(0.05d, 0.0000027);

        public SketchParams(double delta, double epsilon) {
            this.delta = delta;
            this.epsilon = epsilon;
        }

        static SketchParams create(double delta, double epsilon) {
            return new SketchParams(delta, epsilon);
        }

        static SketchParams create() {
            return DEFAULT;
        }

        int getNumRows() {
            return (int) Math.ceil(Math.log(1d / delta));
        }

        int getNumCols() {
            return (int) Math.ceil(EULER_NUM / epsilon);
        }

        double getProbability() {
            return 1 - delta;
        }

        @Override
        public String toString() {
            return "SketchParams{" +
                    "delta=" + delta +
                    ", epsilon=" + epsilon +
                    ", numRows=" + getNumRows() +
                    ", numCols=" + getNumCols() +
                    '}';
        }
    }


    private static class DataValueFrequency {
        private final String dataValue;
        private final int frequency;

        public DataValueFrequency(String dataValue, int frequency) {
            this.dataValue = dataValue;
            this.frequency = frequency;
        }

        @Override
        public String toString() {
            return String.format("%s -> %d", dataValue, frequency);
        }
    }

    static void test() {
        // actual video views on youtube is 5 billion a day (as of 08/14/20)
        long viewsPerMinute = 5 * 1000_1000_1000L / 24 / 60; // which is a whopping 347_256_947;
        // I could not find data on the number of unique videos viewed per day so I am going to
        // assume a number that can turn out to be very in accurate but note that popular videos are viewed frequently
        // which contributes to reducing the number of unique videos viewed
        int uniqueVideosPerMinute = 1000_000;

        String[] uniqueVideoIds = new String[uniqueVideosPerMinute];
        for (int i = 0; i < uniqueVideosPerMinute; i++) {
            uniqueVideoIds[i] = String.format("id%d", i);
        }
        System.out.printf("created unique videos of length: %d%n", uniqueVideosPerMinute);

        Random random = new Random();
        CountMinSketchAlgorithm sketch = new CountMinSketchAlgorithm(SketchParams.DEFAULT);

        // we are going to favor viewing the first 10,000 videos with an extra 10% probability
        int topIndexFavoredVideos = 10_000;
        for (long i = 0; i < viewsPerMinute; i++) {
            if (i % 1000_000 == 0) {
                System.out.printf("sketching at index %d%n", i);
            }
            int videoIndex = random.nextInt(uniqueVideosPerMinute);
            if (videoIndex < uniqueVideosPerMinute * 0.1) {
                // a random video from the favorite set
                sketch.onDataValue(uniqueVideoIds[random.nextInt(topIndexFavoredVideos)]);
            } else {
                sketch.onDataValue(uniqueVideoIds[videoIndex]);
            }
        }
        System.out.printf("finished sketching %d videos for %d views%n", uniqueVideosPerMinute, viewsPerMinute);
        // now that the sketch has the frequency data lets retrieve the top 1000 and make sure they belong in the favored videos
        int sampleSize = 1000;
        PriorityQueue<DataValueFrequency> minHeap = new PriorityQueue<>(Comparator.comparingInt(dvf -> dvf.frequency));

        for (String id : uniqueVideoIds) {
            DataValueFrequency dataValueFrequency = new DataValueFrequency(id, sketch.dataValueFrequency(id));
            if (minHeap.size() < sampleSize) {
                minHeap.add(dataValueFrequency);
            } else if (dataValueFrequency.frequency > minHeap.peek().frequency) {
                minHeap.remove();
                minHeap.add(dataValueFrequency);
            }
        }
        System.out.printf("finished sorting videos in heap of size %d%n", sampleSize);

        // print the heap and check the data values
        int numVideosNotInFavoriteList = 0;
        while (!minHeap.isEmpty()) {
            DataValueFrequency dataValueFrequency = minHeap.remove();

            System.out.printf("%s%n", dataValueFrequency);
            int index = Integer.parseInt(dataValueFrequency.dataValue.substring(2));
            if (index > topIndexFavoredVideos) {
                System.out.println("^^^^^^^^^^^^^^^^^^");
                numVideosNotInFavoriteList++;
            }
        }
        System.out.printf("number of videos not in the favorite list: %d%n", numVideosNotInFavoriteList);
    }
}
