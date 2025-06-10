package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My NON-solution to a Google interview question. I realized too late that this solution works only if the movement in a
 * direction is associated with a coordinate
 * <p>
 * Given the relative positions (S, W, N, E, SW, NW, SE, NE) of some pairs of points on a 2D plane, determine whether
 * it is possible. No two points have the same coordinates.
 * <p>
 * e.g., if the input is "p1 SE p2, p2 SE p3, p3 SE p1", output "impossible".
 * <p>
 * Created by haytham.aldokanji on 6/1/16.
 */
public class RelativePointPositionsInPlain {
    private final Map<String, Point> plain = new HashMap<>();

    public static void main(String[] args) {
        test1();
        test2();
        test3();
    }

    private static void test1() {
        RelativePointPositionsInPlain plain = new RelativePointPositionsInPlain();
        String[] topography = new String[]{"p1 SE p2", "p2 SE p3", "p3 SE p1"};
        Optional<String> result = plain.checkTopography(topography);
        assertThat(result.get(), is("p3 SE p1"));
    }

    private static void test2() {
        RelativePointPositionsInPlain plain = new RelativePointPositionsInPlain();
        String[] topography = new String[]{"p1 SE p2", "p2 SE p3", "p3 N p4"};
        Optional<String> result = plain.checkTopography(topography);
        assertThat(result.isPresent(), is(false));
    }

    private static void test3() {
        RelativePointPositionsInPlain plain = new RelativePointPositionsInPlain();
        String[] topography = new String[]{"p1 SE p2", "p2 SE p3", "p3 N p4"};
        Optional<String> result = plain.checkTopography(topography);
        assertThat(result.isPresent(), is(false));
    }

    public Optional<String> checkTopography(String[] topography) {
        for (String relativePosition : topography) {
            String[] parts = relativePosition.split(" ");

            String pid1 = parts[0];
            Direction direction = Direction.valueOf(parts[1]);
            String pid2 = parts[2];

            if (plain.keySet().containsAll(Sets.newHashSet(pid1, pid2))) {
                boolean valid = isValidPosition(plain.get(pid1), plain.get(pid2), direction);
                if (!valid) {
                    return Optional.of(relativePosition);
                }
            } else if (!(plain.containsKey(pid1) || (plain.containsKey(pid2)))) {
                plain.put(pid2, new Point(pid2, 0, 0));
                Point point1 = nextPoint(plain.get(pid2), pid1, direction);
                plain.put(pid1, point1);
            } else if (!plain.containsKey(pid1)) {
                Point point1 = nextPoint(plain.get(pid2), pid1, direction);
                plain.put(pid1, point1);
            } else if (!plain.containsKey(pid2)) {
                Point point2 = nextPoint(plain.get(pid1), pid2, Direction.reverse(direction));
                plain.put(pid2, point2);
            }
        }
        return Optional.empty();
    }

    private Point nextPoint(Point current, String nextPointId, Direction direction) {
        int xDelta = 0, yDelta = 0;
        switch (direction) {
            case N:
                yDelta++;
                break;
            case S:
                yDelta--;
                break;
            case E:
                xDelta++;
                break;
            case W:
                xDelta--;
                break;
            case NE:
                yDelta++;
                xDelta++;
                break;
            case NW:
                yDelta++;
                xDelta--;
                break;
            case SE:
                yDelta--;
                xDelta++;
                break;
            case SW:
                yDelta--;
                xDelta--;
                break;
            default:
                throw new IllegalStateException("Direction " + direction + " is not supported");
        }
        return new Point(nextPointId, current.x + xDelta, current.y + yDelta);
    }

    private boolean isValidPosition(Point point1, Point point2, Direction direction) {
        switch (direction) {
            case N:
                return point1.y > point2.y;
            case S:
                return point1.y < point2.y;
            case E:
                return point1.x > point2.x;
            case W:
                return point1.x < point2.x;
            case NE:
                return point1.y > point2.y & point1.x > point2.x;
            case NW:
                return point1.y > point2.y && point1.x < point2.x;
            case SE:
                return point1.y < point2.y && point1.x > point2.x;
            case SW:
                return point1.x < point2.x && point1.y < point2.y;
            default:
                throw new IllegalStateException("Direction " + direction + " is not supported");
        }

    }

    private enum Direction {
        N, S, E, W, NE, NW, SE, SW;

        public static Direction reverse(Direction direction) {
            switch (direction) {
                case N:
                    return S;
                case S:
                    return N;
                case E:
                    return W;
                case W:
                    return E;
                case NE:
                    return SW;
                case NW:
                    return SE;
                case SW:
                    return NE;
                case SE:
                    return NW;
                default:
                    throw new IllegalStateException("Direction " + direction + " is not supported");
            }
        }
    }

    private static class Point {
        private final String id;
        private final int x, y;

        public Point(String id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return id + "@(" + x + ", " + y + ")";
        }
    }
}
