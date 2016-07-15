package org.haldokan.edge.interviewquest.google;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - The problem does not state that the 1's should be connected which makes it
 * much simpler (for a more complicated problem similar to this examine my solution to another Google question:
 * MarkingFullyEnclosedRegionsInMatrix). This is a relatively simple problem that in essence is sorting based on 2 criteria.
 * It is possible that the interviewee misunderstood the question or it is not a Google question to start with.
 * The Question: 3_STAR
 * <p>
 * given a board with black (1) and white (0), black are all connected. find the min rectangle that contains all black.
 * <p>
 * example:
 * 0 0 0 0 0
 * 0 1 1 1 0
 * 0 1 1 0 0
 * 0 1 0 0 0
 * 0 0 0 0 0
 * <p>
 * the min rectangle contains all black (1) is the rectangle from (1,1) - (3, 3)
 * <p>
 * Created by haytham.aldokanji on 7/15/16.
 */
public class FindingMinimumEnclosingRegionInMatrix {

    public static void main(String[] args) {
        FindingMinimumEnclosingRegionInMatrix driver = new FindingMinimumEnclosingRegionInMatrix();
        driver.test();
    }

    public int[][] minRectangle(int[][] matrix) {
        int[] minCoord = new int[]{matrix.length, matrix.length};
        int[] maxCoord = new int[]{-1, -1};

        for (int i = 0; i < matrix.length; i++) {
            int[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                if (row[j] == 1) {
                    if (i < minCoord[0]) {
                        minCoord[0] = i;
                    }
                    if (j < minCoord[1]) {
                        minCoord[1] = j;
                    }
                    if (i > maxCoord[0]) {
                        maxCoord[0] = i;
                    }
                    if (j > maxCoord[1]) {
                        maxCoord[1] = j;
                    }
                }
            }
        }
        return new int[][]{minCoord, maxCoord};
    }

    private void test() {
        int[][] matrix = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0},
                {0, 1, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {1, 0, 0, 0, 0},
        };

        int[][] result = minRectangle(matrix);
        assertThat(result[0], is(new int[]{1, 0}));
        assertThat(result[1], is(new int[]{5, 3}));
    }
}

