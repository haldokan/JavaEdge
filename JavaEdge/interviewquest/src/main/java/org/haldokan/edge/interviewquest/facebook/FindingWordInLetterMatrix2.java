package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayList;
import java.util.List;

/**
 * My solution to a Facebook interview question - used DFS to descend the matrix at every point to find the word. It is
 * more elegant and concise than using BFS (check my other implementation: FindingWordInLetterMatrixFacebook)
 * The Question: 5_STAR
 * Given a matrix of letters and a word, check if the word is present in the matrix. E,g., suppose matrix is:
 * a b c d e f
 * z n a b c f
 * f g f a b c
 * and given word is fnz, it is present. However, gng is not since you would be repeating g twice.
 * You can move in all the 8 directions around an element.
 * <p>
 * Created by haytham.aldokanji on 6/24/20.
 */
public class FindingWordInLetterMatrix2 {
    public static void main(String[] args) {
        FindingWordInLetterMatrix2 driver = new FindingWordInLetterMatrix2();
        driver.test1();
    }

    private void test1() {
        char[][] matrix = {
            {'B', 'F', 'A', 'B', 'B', 'N'},
            {'B', 'A', 'C', 'B', 'B', 'O'},
            {'B', 'B', 'E', 'B', 'B', 'Z'},
            {'N', 'O', 'B', 'K', 'B', 'A'},
            {'B', 'B', 'O', 'O', 'B', 'M'},
            {'B', 'B', 'B', 'B', 'B', 'A'}
        };
        System.out.println(find(matrix, "FACEBOOK"));
        System.out.println(find(matrix, "FACE"));
        System.out.println(find(matrix, "BOOK"));
        System.out.println(find(matrix, "FAKEBOOK"));
        System.out.println(find(matrix, "FAKE"));
    }

    boolean find(char[][] matrix, String word) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int[] point = new int[]{i, j};
                if (doFind(matrix, word, point, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean doFind(char[][] matrix, String word, int[] point, int position) {
        if (position == word.length()) {
//            System.out.printf("%s - %d%n", Arrays.toString(point), position);
            return true;
        }
        if (matrix[point[0]][point[1]] == word.charAt(position)) {
            List<int[]> neighbors = neighbors(matrix, point);
            for (int[] neighbor : neighbors) {
                if (doFind(matrix, word, neighbor, position + 1)) {
                    return true;
                }
            }
        }
//        System.out.printf("-->%s - %d%n", Arrays.toString(point), position);
        return false;
    }

    // for this excercise I assume perpendicular moves only (diagonal moves does not make a difference to the gist of the question)
    List<int[]> neighbors(char[][] matrix, int[] point) {
        int x = point[0];
        int y = point[1];
        List<int[]> neighbors = new ArrayList<>();

        if (x + 1 < matrix.length) {
            neighbors.add(new int[]{x + 1, y});
        }
        if (x - 1 >= 0) {
            neighbors.add(new int[]{x - 1, y});
        }
        if (y + 1 < matrix[0].length) {
            neighbors.add(new int[]{x, y + 1});
        }
        if (y - 1 >= 0) {
            neighbors.add(new int[]{x, y - 1});
        }

        return neighbors;
    }
}
