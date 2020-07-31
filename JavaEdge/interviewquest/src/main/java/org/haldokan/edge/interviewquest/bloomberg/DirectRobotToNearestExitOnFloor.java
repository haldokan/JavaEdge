/**
 * My solution to a Bloomberg interview question - no surprises: BFS marking visited landmarks. keeping track of the path
 * and converting that to directions adds a bit of flavor to the question.
 * <p>
 * The Question: 4-STAR
 * <p>
 * Direct a robot on a building floor to the nearest exit. The robot can go thru doors but it cannot smash it's way thru walls
 */
package org.haldokan.edge.interviewquest.bloomberg;

import java.util.*;

public class DirectRobotToNearestExitOnFloor {
    public static void main(String[] args) {
        DirectRobotToNearestExitOnFloor driver = new DirectRobotToNearestExitOnFloor();
        driver.test();
    }

    String getDirection(Position previous, Position position) {
        if (previous != null) {
            if (previous.x > position.x) {
                return "UP";
            }
            if (previous.x < position.x) {
                return "DOWN";
            }
            if (previous.y > position.y) {
                return "LEFT";
            }
            if (previous.y < position.y) {
                return "RIGHT";
            }
        }
        return "";
    }

    String makeDirections(Map<Position, Position> ancestors, Position exit) {
        Position position = exit;
        StringBuilder directions = new StringBuilder();

        while (position != null) {
            Position parent = ancestors.get(position);
            directions.insert(0, "->").insert(0, getDirection(parent, position));
            position = parent;
        }
        return String.format("%s%s%s", "START", directions.toString(), "EXIT");
    }

    String findExit(String[][] floor) {
        Position start = new Position(0, 0);
        List<Position> queue = new LinkedList<>();
        queue.add(start);

        Map<Position, Position> ancestors = new HashMap<>();
        ancestors.put(start, null);
        Set<Position> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            Position currPos = queue.remove(0);
            visited.add(currPos);

            List<Position> neighbors = neighbors(floor, currPos);
            for (Position neighbor : neighbors) {
                String cell = floor[neighbor.x][neighbor.y];

                if ((cell.equals("D") || cell.equals("E")) && !visited.contains(neighbor)) {
                    ancestors.put(neighbor, currPos);
                    queue.add(neighbor);
                }
                if (cell.equals("E")) {
                    return makeDirections(ancestors, neighbor);
                }
            }
        }
        return null;
    }

    List<Position> neighbors(String[][] floor, Position pos) {
        int x = pos.x;
        int y = pos.y;

        List<Position> neighors = new ArrayList<>();
        if (x - 1 >= 0) {
            neighors.add(new Position(x - 1, y));
        }
        if (x + 1 < floor.length) {
            neighors.add(new Position(x + 1, y));
        }
        if (y - 1 >= 0) {
            neighors.add(new Position(x, y - 1));

        }
        if (y + 1 < floor[0].length) {
            neighors.add(new Position(x, y + 1));

        }
        return neighors;
    }

    static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x &&
                    y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
    }

    String[][] makeFloor() {
        String[][] floor = new String[][]{
                new String[]{"W", "D", "D", "D"},
                new String[]{"D", "D", "W", "D"},
                new String[]{"W", "W", "D", "D"},
                new String[]{"E", "W", "D", "W"},
                new String[]{"D", "D", "D", "E"},
        };
        return floor;
    }

    void test() {
        String path = findExit(makeFloor());
        System.out.println(path);
    }
}
