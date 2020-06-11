package org.haldokan.edge.interviewquest.google;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - I used a dynamic-programming-flavored solution to do n-way merge among
 * the arrays. Space and time complexity are (O(n)) where n is the sum of the arrays lengths. This solution is better than
 * the one proposed on Careercup suggesting using a min heap since the cost of other other algorithm is O(nlogn).
 * <p>
 * The Question (4_STARS):
 *
 * Write a Code to merge N sorted array.
 * <p>
 * Created by haytham.aldokanji on 5/27/16.
 */
public class GenerifiedNWaySortedArrayMerge<T extends Comparable<T>> {

    public static void main(String[] args) {
        testMerge();
    }

    private static void testMerge() {
        Integer[][] arrays = new Integer[][]{
                {1, 2},
                {3},
                {2, 4},
                {5, 8, 11},
                {6, 7, 9, 12},
                {1, 3, 5, 7, 9, 11, 13},
                {-4, -3},
                {17, 19}
        };

        GenerifiedNWaySortedArrayMerge<Integer> merger = new GenerifiedNWaySortedArrayMerge<>();

        Integer[] merged = merger.merge(arrays, Integer.class);
        System.out.println(Arrays.toString(merged));
        assertThat(merged, is(new Integer[]{-4, -3, 1, 1, 2, 2, 3, 3, 4, 5, 5, 6, 7, 7, 8, 9, 9, 11, 11, 12, 13, 17, 19}));
    }

    public T[] merge(T[][] arrays, Class<T> klass) {
        if (arrays == null || arrays.length == 0) {
            throw new IllegalArgumentException("Null or empty input");
        }
        if (arrays.length == 1) {
            return arrays[0];
        }

        @SuppressWarnings("unchecked")
        T[] mergedArr = (T[]) Array.newInstance(klass,
            Arrays.stream(arrays).mapToInt(array -> array.length).sum());

        int[] activeIndexes = new int[arrays.length];
        int mergeIndex = 0;
        for (; ; ) {
            Optional<int[]> potentialMinLocation = locateMinVal(activeIndexes, arrays);
            if (!potentialMinLocation.isPresent()) {
                break;
            }
            int[] minLocation = potentialMinLocation.get();
            mergedArr[mergeIndex++] = arrays[minLocation[0]][minLocation[1]];

            int nextIndex = minLocation[1] + 1;
            if (nextIndex > arrays[minLocation[0]].length - 1) {
                activeIndexes[minLocation[0]] = -1;
            } else {
                activeIndexes[minLocation[0]] = nextIndex;
            }
        }
        return mergedArr;
    }

    private Optional<int[]> locateMinVal(int[] activeIndexes, T[][] arrays) {
        int indexOfActiveIndex = -1;
        for (int i = 0; i < activeIndexes.length; i++) {
            if (activeIndexes[i] != -1) {
                indexOfActiveIndex = i;
                break;
            }
        }
        if (indexOfActiveIndex == -1) {
            return Optional.empty();
        }
        int activeIndex = activeIndexes[indexOfActiveIndex];
        int[] minLocation = new int[]{indexOfActiveIndex, activeIndex};

        for (int i = 0; i < activeIndexes.length; i++) {
            activeIndex = activeIndexes[i];

            if (activeIndex != -1) {
                if (arrays[i][activeIndex].compareTo(arrays[minLocation[0]][minLocation[1]]) <= 0) {
                    minLocation[0] = i;
                    minLocation[1] = activeIndex;
                }
            }
        }
        return Optional.of(minLocation);
    }
}
