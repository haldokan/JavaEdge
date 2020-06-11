package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - used my other solution for a different Google question
 * GenerifiedNWaySortedArrayMerge to merge the tuples. Solution works for any number of tuples.
 *
 * The Question: 4_STAR
 *
 * There is an array of 3-tuple, in the form of (a, 1, 5). The first element in the tuple is the id, the second and
 * third elements are both integers, and the third is always larger than or equal to the second. Assume that the array
 * is sorted based on the second element of the tuple. Write a function that breaks each of the 3-tuple into
 * two 2-tuples like (a, 1) and (a, 5), and sort them according to the integer.
 * <p>
 * E.g. given (a, 1, 5), (b, 2, 4), (c, 7, 8)
 * output (a, 1), (b, 2), (b, 4), (a, 5), (c, 7), (c, 8)
 * <p>
 * Created by haytham.aldokanji on 6/5/16.
 */
public class SortingArrayOfTuples {

    public static void main(String[] args) {
        SortingArrayOfTuples driver = new SortingArrayOfTuples();
        driver.test();
    }

    public Tuple2[] sort(String[][] arr3) {
        Tuple2[][] arr2 = new Tuple2[arr3.length][2];
        int index = 0;
        for (String[] tuple3 : arr3) {
            arr2[index++] = new Tuple2[]{new Tuple2(tuple3[0], tuple3[1]), new Tuple2(tuple3[0], tuple3[2])};
        }

        GenerifiedNWaySortedArrayMerge<Tuple2> merger = new GenerifiedNWaySortedArrayMerge<>();
        return merger.merge(arr2, Tuple2.class);
    }

    private void test() {
        String[][] arr3 = new String[][]{
                {"a", "1", "5"},
                {"b", "2", "4"},
                {"c", "7", "8"},
                {"d", "0", "9"}
        };
        Tuple2[] result = sort(arr3);
        assertThat(Arrays.stream(result).map(Tuple2::toString).collect(Collectors.joining(", ")),
                is("(d, 0), (a, 1), (b, 2), (b, 4), (a, 5), (c, 7), (c, 8), (d, 9)"));
    }

    private static class Tuple2 implements Comparable<Tuple2> {
        private final String id;
        private final int value;

        public Tuple2(String id, String value) {
            this.id = id;
            this.value = Integer.parseInt(value);
        }

        @Override
        public int compareTo(Tuple2 other) {
            return value - other.value;
        }

        @Override
        public String toString() {
            return "(" + id + ", " + value + ")";
        }
    }
}
