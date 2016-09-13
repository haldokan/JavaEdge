package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - since matrix is sparse we can use a map instead of 2d array (similar to
 * representing a graph using adjacency list). In this solution we achieve O(1) time complexity for 'set' and O(n) time
 * complexity for 'sum' where n is the number of set elements. Space complexity is O(n).
 * <p>
 * Solution would have been slightly better if instead of using Map<String, Integer> I used
 * Map<Integer, Map<Integer, Integer>>. Or, in a real app, use Guava's HashBasedTable (which is a complete implementation
 * of what I present here).
 * <p>
 * The Question: 3_STAR
 * <p>
 * Given a sparse matrix, implement below two methods:
 * void set(int row, int col, int val) - update value at given row and col
 * <p>
 * int sum(int row, int col) - give sum from top left corner to given row, col sub-matrix
 * <p>
 * Created by haytham.aldokanji on 9/13/16.
 */
public class SetAndSumOnSparseMatrix {
    private final Map<String, Integer> sparseMatrix = new HashMap<>();

    public static void main(String[] args) {
        SetAndSumOnSparseMatrix driver = new SetAndSumOnSparseMatrix();
        driver.testSet();
        driver.testSum();
    }

    public void set(int row, int col, int val) {
        sparseMatrix.put(makeCoordinate(row, col), val);
    }

    public int get(int row, int col) {
        return sparseMatrix.get(makeCoordinate(row, col));
    }

    public int sum(int row, int col) {
        return sparseMatrix.entrySet().stream()
                .filter(e -> {
                    int[] coordinate = getCoordinate(e.getKey());
                    return coordinate[0] <= row && coordinate[1] <= col;
                })
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    private String makeCoordinate(int row, int col) {
        return row + "," + col;
    }

    private int[] getCoordinate(String key) {
        return Arrays.stream(key.split(",")).mapToInt(Integer::valueOf).toArray();
    }

    private void testSet() {
        set(3, 500, 10);
        assertThat(get(3, 500), is(10));

        set(5000, 700, 20);
        assertThat(get(5000, 700), is(20));

        set(3, 500, 15);
        assertThat(get(3, 500), is(15));
    }

    private void testSum() {
        set(3, 500, 10);
        assertThat(get(3, 500), is(10));

        set(5000, 700, 20);
        assertThat(get(5000, 700), is(20));

        int sum = sum(5001, 701);
        assertThat(sum, is(30));

        sum = sum(5000, 699);
        assertThat(sum, is(10));
    }
}
