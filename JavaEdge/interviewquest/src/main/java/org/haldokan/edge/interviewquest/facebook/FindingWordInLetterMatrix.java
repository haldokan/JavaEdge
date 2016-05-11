package org.haldokan.edge.interviewquest.facebook;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - suggestive of some graph algorithms (MST for instance)
 *
 * Given a matrix of letters and a word, check if the word is present in the matrix. E,g., suppose matrix is:
 * a b c d e f
 * z n a b c f
 * f g f a b c
 * and given word is fnz, it is present. However, gng is not since you would be repeating g twice.
 *
 * You can move in all the 8 directions around an element.
 * <p>
 * Created by haytham.aldokanji on 5/8/16.
 */
public class FindingWordInLetterMatrix {
    private static final char[][] matrix = new char[][]{
            {'s', 's', 'l', 'd', 'u', 'f'},
            {'m', 'o', 'm', 'u', 'i', 's'},
            {'i', 'i', 'o', 'c', 'k', 'a'},
            {'t', 't', 'h', 'c', 'e', 'r'}
    };
    private static final int MATRIX_WIDTH = matrix[0].length;

    public static void main(String[] args) {
        FindingWordInLetterMatrix driver = new FindingWordInLetterMatrix();

        driver.test1();
        driver.test2();
        driver.test3();
        driver.test4();
        driver.test5();
        driver.test6();
        driver.test7();
        driver.test8();
        driver.test9();
    }

    // return the path for finding the word in the format
    public Optional<MatrixCell[]> find(String word) {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Null of empty input: " + word);
        }

        Set<MatrixCell> usedPathStarts = new HashSet<>();
        Deque<MatrixCell> evalStack = new ArrayDeque<>();

        boolean wordFound = false;
        boolean morePathsExist = true;

        while (!wordFound && morePathsExist) {
            Optional<MatrixCell> potentialPathStart = findPathStart(usedPathStarts, word.charAt(0));
            if (!potentialPathStart.isPresent()) {
                morePathsExist = false;
                continue;
            }
            MatrixCell pathStart = potentialPathStart.get();
            Deque<Character> letterQueue = queueWordLetters(word);

            Map<MatrixCell, Set<MatrixCell>> cellVisitedNeighbors = new HashMap<>();
            evalStack.push(pathStart);
            letterQueue.remove();

            while (!(evalStack.isEmpty() || letterQueue.isEmpty())) {
                MatrixCell fromCell = evalStack.peek();
                char nextLetter = letterQueue.peekFirst();
                Optional<MatrixCell> linkedCell = advanceToNextCell(fromCell, nextLetter, evalStack, cellVisitedNeighbors);

                if (!linkedCell.isPresent()) {
                    evalStack.pop();
                    letterQueue.addFirst(matrix[fromCell.row][fromCell.column]);
                } else {
                    evalStack.push(linkedCell.get());
                    letterQueue.remove();
                }
            }
            if (letterQueue.isEmpty()) {
                wordFound = true;
            }
        }
        return getPath(evalStack);
    }

    private Deque<Character> queueWordLetters(String word) {
        char[] letters = word.toCharArray();
        Deque<Character> letterQueue = new ArrayDeque<>();

        for (char letter : letters) {
            letterQueue.add(letter);
        }
        return letterQueue;
    }

    private Optional<MatrixCell> findPathStart(Set<MatrixCell> usedPathStarts, char letter) {
        for (int i = 0; i < matrix.length; i++) {
            char[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                MatrixCell cell = new MatrixCell(i, j);
                if (!usedPathStarts.contains(cell) && matrix[i][j] == letter) {
                    usedPathStarts.add(cell);
                    return Optional.of(cell);
                }
            }
        }
        return Optional.empty();
    }

    private Optional<MatrixCell> advanceToNextCell(MatrixCell fromCell, char letter, Deque<MatrixCell> evalStack, Map<MatrixCell, Set<MatrixCell>> cellVisitedNeighbors) {

        if (cellVisitedNeighbors.get(fromCell) == null) {
            cellVisitedNeighbors.put(fromCell, new HashSet<>());
        }
        Optional<MatrixCell> linkedCell = Optional.empty();

        for (int i = fromCell.row - 1; i <= fromCell.row + 1; i++) {
            for (int j = fromCell.column - 1; j <= fromCell.column + 1; j++) {
                MatrixCell toCell = new MatrixCell(i, j);
                if (!cellVisitedNeighbors.get(fromCell).contains(toCell)
                        && !evalStack.contains(toCell)
                        && !toCell.equals(fromCell)
                        && (toCell.row >= 0 && toCell.row < matrix.length)
                        && (toCell.column >= 0 && toCell.column < MATRIX_WIDTH)
                        && matrix[toCell.row][toCell.column] == letter) {
                    linkedCell = Optional.of(toCell);
                    cellVisitedNeighbors.get(fromCell).add(toCell);
                    break;
                }
            }
        }
        return linkedCell;
    }

    private Optional<MatrixCell[]> getPath(Deque<MatrixCell> evalStack) {
        if (evalStack.isEmpty()) {
            return Optional.empty();
        }

        MatrixCell[] path = new MatrixCell[evalStack.size()];
        int index = 0;
        while (!evalStack.isEmpty()) {
            path[index++] = evalStack.removeLast();
        }
        return Optional.of(path);
    }

    private String fromPathToWord(MatrixCell[] path) {
        StringBuilder word = new StringBuilder(path.length);
        for (MatrixCell cell : path) {
            word.append(matrix[cell.row][cell.column]);
        }
        return word.toString();
    }

    private void test1() {
        String word = "ducksarecool";
        Optional<MatrixCell[]> path = find(word);
        assertThat(path.isPresent(), is(true));
        assertThat(fromPathToWord(path.get()), is(word));
    }

    private void test2() {
        String word = "smith";
        Optional<MatrixCell[]> path = find(word);
        assertThat(path.isPresent(), is(true));
        assertThat(fromPathToWord(path.get()), is(word));
    }

    private void test3() {
        String word = "moms";
        Optional<MatrixCell[]> path = find(word);
        assertThat(path.isPresent(), is(true));
        assertThat(fromPathToWord(path.get()), is(word));
    }

    private void test4() {
        String word = "homom";
        Optional<MatrixCell[]> path = find(word);
        assertThat(path.isPresent(), is(true));
        assertThat(fromPathToWord(path.get()), is(word));
    }

    private void test5() {
        String word = "homomss";
        Optional<MatrixCell[]> path = find(word);
        assertThat(path.isPresent(), is(true));
        assertThat(fromPathToWord(path.get()), is(word));
    }

    private void test6() {
        String word = "homomsm";
        Optional<MatrixCell[]> path = find(word);
        assertThat(path.isPresent(), is(false));
    }

    private void test7() {
        String word = "homomss";
        Optional<MatrixCell[]> path = find(word);
        assertThat(path.isPresent(), is(true));
        assertThat(fromPathToWord(path.get()), is(word));
    }

    private void test8() {
        String word = "homomsso";
        Optional<MatrixCell[]> path = find(word);
        assertThat(path.isPresent(), is(false));
    }

    private void test9() {
        String word = "homomsk";
        Optional<MatrixCell[]> path = find(word);
        assertThat(path.isPresent(), is(false));
    }

    private static class MatrixCell {
        private final int row, column;

        public MatrixCell(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MatrixCell cell = (MatrixCell) o;

            if (row != cell.row) return false;
            return column == cell.column;

        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + column;
            return result;
        }

        @Override
        public String toString() {
            return "(" + row + ", " + column + ")";
        }
    }
}
