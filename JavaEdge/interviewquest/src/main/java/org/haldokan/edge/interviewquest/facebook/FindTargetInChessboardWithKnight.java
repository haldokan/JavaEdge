package org.haldokan.edge.interviewquest.facebook;

import java.util.*;
import java.util.stream.Collectors;

/**
 * My solution to a facebook interview question - BFS with a twist for the knight quirky move
 *
 * The Question: 4-STAR
 *
 * find target in chess board with given start position of knight
 * 11/28/20
 */
public class FindTargetInChessboardWithKnight {
    private static final int BOARD_DIM = 8;

    public static void main(String[] args) {
        FindTargetInChessboardWithKnight driver = new FindTargetInChessboardWithKnight();
        driver.test1();
        System.out.println("-------");
        driver.test2();
    }

    List<Square> findTarget(boolean[][] board, Square start, Square target) {
        LinkedList<Square> queue = new LinkedList<>();
        queue.add(start);

        Map<Square, Square> ancestors = new HashMap<>();
        ancestors.put(start, null);

        Square current;
        while(!queue.isEmpty()) {
            current = queue.removeFirst();
            if (current.x == target.x && current.y == target.y) {
                return getPath(target, ancestors);
            }
            board[current.x][current.y] = true;
            List<Square> children = getChildren(board, current);
            for (Square child : children) {
                ancestors.put(child, current);
                queue.addLast(child);
            }
        }
        throw new IllegalStateException("Algorithm is wrong: Horse (i.e. knight!) should always find target");
    }

    private List<Square> getPath(Square target, Map<Square, Square> ancestors) {
        LinkedList<Square> path = new LinkedList<>();
        Square current = target;
        while (current != null) {
            path.addFirst(current);
            current = ancestors.get(current);
        }
        return path;
    }

    private List<Square> getChildren(boolean[][] board, Square square) {
        List<Square> children = new ArrayList<>();

        for (int offset : new int[]{2, -2}) {
            int x = square.x + offset;
            if ( x >= 0 && x < BOARD_DIM) {
                int y = square.y;
                if (y + 1 < BOARD_DIM) {
                    children.add(Square.create(x, y + 1));
                }
                if (y - 1 >= 0) {
                    children.add(Square.create(x, y - 1));
                }
            }

            int y = square.y + offset;
            if ( y >= 0 && y < BOARD_DIM) {
                int x1 = square.x;
                if (x1 + 1 < BOARD_DIM) {
                    children.add(Square.create(x1 + 1, y));
                }
                if (x1 - 1 >= 0) {
                    children.add(Square.create(x1 - 1, y));
                }
            }
        }
        // filter-in only unvisited squares
        return children.stream().filter(pos -> !board[pos.x][pos.y]).collect(Collectors.toList());
    }

    private static final class Square {
        private final int x;
        private final int y;

        public static Square create(int x, int y) {
            return new Square(x, y);
        }

        private Square(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Square square = (Square) o;
            return x == square.x &&
                    y == square.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    private void test1() {
        boolean[][] board = new boolean[BOARD_DIM][BOARD_DIM];
        List<Square> path = findTarget(board, Square.create(7, 1), Square.create(0, 6));
        path.forEach(System.out::println);
    }

    private void test2() {
        boolean[][] board = new boolean[BOARD_DIM][BOARD_DIM];
        List<Square> path = findTarget(board, Square.create(0, 1), Square.create(0,0));
        path.forEach(System.out::println);
    }
}
