package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - used counting sort
 * NOTE: the correct solution is to use a combination of 2 min/max heaps as described here:
 * https://www.geeksforgeeks.org/median-of-stream-of-integers-running-integers/
 * It is certainly not something and interviewee can figure out on the spot. Check the implementation I provide in
 * MedianOfStreamingData2
 * <p
 * The Question: 4_STAR
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
 * Created by haytham.aldokanji on 5/6/16.
 */
public class MedianOfStreamingData {
    private final Map<Integer, Integer> data = new TreeMap<>();

    public static void main(String[] args) {
        MedianOfStreamingData driver = new MedianOfStreamingData();
        driver.test1();
        driver.test2();
        driver.test3();
        driver.test4();
        driver.test5();
        driver.test6();
        driver.test7();
    }

    public void addNumber(Integer number) {
        data.compute(number, (k, v) -> v == null ? 1 : v + 1);
    }

    public Double median() {
        Integer dataLength = data.values().stream().reduce(Integer::sum).get();
        Integer medianIndex1 = dataLength / 2 + dataLength % 2;
        Integer medianIndex2 = dataLength % 2 == 0 ? medianIndex1 + 1 : medianIndex1;

        Optional<Integer>[] medianValues = valuesAt(medianIndex1, medianIndex2);
        Integer sum = medianValues[0].get() + medianValues[1].get();
        return Double.parseDouble(String.valueOf(sum)) / 2d;
    }

    private Optional<Integer>[] valuesAt(int index1, int index2) {
        Collection<Map.Entry<Integer, Integer>> entrySet = data.entrySet();
        Optional<Integer>[] result = new Optional[]{Optional.empty(), Optional.empty()};

        int index = 0;
        for (Map.Entry<Integer, Integer> entry : entrySet) {
            index += entry.getValue();
            if (!result[0].isPresent() && index1 <= index) {
                result[0] = Optional.of(entry.getKey());
            }
            if (index2 <= index) {
                result[1] = Optional.of(entry.getKey());
            }
            if (result[0].isPresent() && result[1].isPresent()) {
                break;
            }
        }
        return result;
    }

    private void test1() {
        data.clear();
        Arrays.stream(new int[]{2, 7, 4, 9, 1, 5, 8, 3, 6}).forEach(this::addNumber);
        assertThat(median(), is(5d));
    }

    private void test2() {
        data.clear();
        Arrays.stream(new int[]{2, 7, 4, 9, 1, 5, 8, 3, 6, 10}).forEach(this::addNumber);
        assertThat(median(), is(5.5d));
    }

    private void test3() {
        data.clear();
        Arrays.stream(new int[]{30, 19, 17, 22, 12}).forEach(this::addNumber);
        assertThat(median(), is(19d));
    }

    private void test4() {
        data.clear();
        Arrays.stream(new int[]{30, 40, 19, 22, 12, 17}).forEach(this::addNumber);
        assertThat(median(), is(20.5d));
    }

    private void test5() {
        data.clear();
        int[] arr = new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 7, 4, 9, 1, 5, 5, 8, 3, 6, 10};
        assertThat(arr.length % 2, is(0));

        Arrays.stream(arr).forEach(this::addNumber);

        Arrays.sort(arr);
        int midIndex = arr.length / 2 - 1;
        double expectedMedian = (arr[midIndex] + arr[midIndex + 1]) / 2;
        assertThat(median(), is(expectedMedian));
    }

    private void test6() {
        data.clear();
        int[] arr = new int[]{2, 2, 2, 2, 2, 2, 2, 2, 7, 4, 9, 1, 5, 5, 8, 3, 6, 10};
        assertThat(arr.length % 2, is(0));

        Arrays.stream(arr).forEach(this::addNumber);

        Arrays.sort(arr);
        int midIndex = arr.length / 2 - 1;
        double expectedMedian = (arr[midIndex] + arr[midIndex + 1]) / 2d;
        assertThat(median(), is(expectedMedian));
    }

    private void test7() {
        data.clear();
        int[] arr = new int[]{2, 2, 2, 2, 2, 2, 2, 7, 4, 9, 1, 5, 5, 8, 3, 6, 10};
        assertThat(arr.length % 2, is(1));

        Arrays.stream(arr).forEach(this::addNumber);

        Arrays.sort(arr);
        int midIndex = arr.length / 2;
        double expectedMedian = (arr[midIndex]);
        assertThat(median(), is(expectedMedian));
    }

}
