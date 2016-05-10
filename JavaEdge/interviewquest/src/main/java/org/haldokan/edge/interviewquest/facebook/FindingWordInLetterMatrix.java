package org.haldokan.edge.interviewquest.facebook;

import java.util.*;

/**
 * @INPROGRESS My solution to a Facebook interview question
 * Given a matrix of letters and a word, check if the word is present in the matrix. E,g., suppose matrix is:
 * a b c d e f
 * z n a b c f
 * f g f a b c
 * and given word is fnz, it is present. However, gng is not since you would be repeating g twice.
 * You can move in all the 8 directions around an element.
 * <p>
 * Created by haytham.aldokanji on 5/8/16.
 */
public class FindingWordInLetterMatrix {
    // find cell matching the 1st letter - root
    // check all eight neighbors for 2n letter
    // mark cells already matching letters in the word as unavailable
    // when n matching letter found at a cell back up and look at its parent other children: the cell should be marked as
    // unavailable for the parent search. Cell stays unavailable until it's parent is popped from the stack or a different
    // matching child is found
    // repeat for other roots if the current root does not provide a path to a match

    private static final char[][] matrix = new char[][]{
            {'a', 'b', 'c', 'd', 'u', 'f'},
            {'z', 'n', 'a', 'u', 'c', 'f'},
            {'f', 'g', 'f', 's', 'k', 'c'}
    };
    private static final int MATRIX_WIDTH = matrix[0].length;

    public static void main(String[] args) {
        FindingWordInLetterMatrix driver = new FindingWordInLetterMatrix();
        driver.find("ducks");
    }

    // return the path for finding the word in the format [xi, yi]...
    public Optional<int[][]> find(String word) {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Null of empty input: " + word);
        }

        Set<int[]> usedPathStarts = new HashSet<>();
        Deque<int[]> evalStack = new ArrayDeque<>();

        boolean wordFound = false;
        boolean morePathsExist = true;

        while (!wordFound && morePathsExist) {
            Optional<int[]> potentialPathStart = findPathStart(usedPathStarts, word.charAt(0));
            if (!potentialPathStart.isPresent()) {
                morePathsExist = false;
                continue;
            }
            int[] pathStart = potentialPathStart.get();

            Deque<Character> letterQueue = queueWordLetters(word);
            // will see if we need clear
            // wordPath.clear();

            Map<Integer, Set<int[]>> cellVisitedNeighbors = new HashMap<>();
            evalStack.push(pathStart);
            letterQueue.remove();

            while (!(evalStack.isEmpty() || letterQueue.isEmpty())) {
                int[] fromCell = evalStack.peek();
                char nextLetter = letterQueue.peekFirst();
                Optional<int[]> linkedCell = advanceToNextCell(fromCell, nextLetter, evalStack, cellVisitedNeighbors);

                if (!linkedCell.isPresent()) {
                    evalStack.pop();
                    letterQueue.addFirst(matrix[fromCell[0]][fromCell[1]]);
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

    private Optional<int[]> findPathStart(Set<int[]> usedPathStarts, char letter) {
        for (int i = 0; i < matrix.length; i++) {
            char[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                int[] cell = new int[]{i, j};
                if (!usedPathStarts.contains(cell) && matrix[i][j] == letter) {
                    usedPathStarts.add(cell);
                    return Optional.of(cell);
                }
            }
        }
        return Optional.empty();
    }

    private Optional<int[]> advanceToNextCell(int[] fromCell, char letter, Deque<int[]> evalStack, Map<Integer, Set<int[]>> cellVisitedNeighbors) {
        int row = fromCell[0];
        int column = fromCell[1];

        int cellHashCode = Arrays.hashCode(fromCell);
        if (cellVisitedNeighbors.get(cellHashCode) == null) {
            cellVisitedNeighbors.put(cellHashCode, new HashSet<>());
        }
        Optional<int[]> linkedCell = Optional.empty();
        // the start cell is excluded because it is visited
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                int[] cell = new int[]{i, j};
                if (!cellVisitedNeighbors.get(cellHashCode).contains(cell)
                        && !evalStack.contains(cell)
                        && matrix[cell[0]][cell[1]] == letter
                        && (cell[0] >= 0 && cell[0] < matrix.length)
                        && (cell[1] >= 0 && cell[1] < MATRIX_WIDTH)) {
                    linkedCell = Optional.of(cell);
                    cellVisitedNeighbors.get(cellHashCode).add(cell);
                    break;
                }
            }
        }
        return linkedCell;
    }

    private Optional<int[][]> getPath(Deque<int[]> evalStack) {
        if (evalStack.isEmpty()) {
            return Optional.empty();
        }

        int[][] path = new int[evalStack.size()][];
        int index = 0;
        while (!evalStack.isEmpty()) {
            path[index] = evalStack.pop();
        }
        return Optional.of(path);
    }
}
