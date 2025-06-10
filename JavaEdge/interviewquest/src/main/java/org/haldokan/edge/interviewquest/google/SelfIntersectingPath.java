package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - it handles the case of child spirals
 *
 * The Question: 5_STAR
 *
 * You are given a list of n float numbers x_1, x_2, x_3, ... x_n, where x_i > 0.
 * A traveler starts at point (0, 0) and moves x_1 metres to the north, then x_2 metres to the west,
 * x_3 to the south, x_4 to the east and so on (after each lastMove his direction changes counter-clockwise)
 * Write a single-pass algorithm that uses O(1) memory to determine, if the travelers path crosses itself, or not
 * (i.e. if it's self-intersecting)
 * e.g.
 * 2 1 1 2 -> crosses
 * 1 2 3 4 -> doesn't cross
 * <p>
 * Created by haytham.aldokanji on 9/8/15.
 */
public class SelfIntersectingPath {
    private Optional<Edge> upperEdge = Optional.empty();
    private Optional<Edge> bottomEdge = Optional.empty();
    private Optional<Edge> leftEdge = Optional.empty();
    private Optional<Edge> rightEdge = Optional.empty();
    private float[] travelerCoords = new float[]{0, 0};
    private Direction nextDirection = Direction.N;
    private Optional<Edge> verticalShadow = Optional.empty();
    private Optional<Edge> horizontalShadow = Optional.empty();

    public static void main(String[] args) {
        SelfIntersectingPath driver = new SelfIntersectingPath();
        driver.printPath();

        float[] distances = new float[]{3, 2, 4, 3, 5, 4, 2, 2};
        boolean selfIntersecting = driver.move(distances);
        assertThat(selfIntersecting, is(true));

        driver = new SelfIntersectingPath();
        driver.printPath();
        distances = new float[]{3, 2, 4, 3, 5, 4, 2};
        selfIntersecting = driver.move(distances);
        assertThat(selfIntersecting, is(false));

        driver = new SelfIntersectingPath();
        driver.printPath();
        distances = new float[]{3, 2, 4, 3, 5, 4, 6, 2, 1.5f};
        selfIntersecting = driver.move(distances);
        assertThat(selfIntersecting, is(true));

        driver = new SelfIntersectingPath();
        driver.printPath();
        distances = new float[]{3, 2, 4, 3, 5, 4, 6, 7, 4, 3};
        selfIntersecting = driver.move(distances);
        assertThat(selfIntersecting, is(true));

        driver = new SelfIntersectingPath();
        driver.printPath();
        distances = new float[]{3, 2, 4, 3, 5, 4, 6, 7, 7, 4, 1};
        selfIntersecting = driver.move(distances);
        assertThat(selfIntersecting, is(true));

        driver = new SelfIntersectingPath();
        driver.printPath();
        distances = new float[]{3, 2, 4, 3, 5, 4, 6, 7, 7, 2, 7};
        selfIntersecting = driver.move(distances);
        assertThat(selfIntersecting, is(true));

        driver = new SelfIntersectingPath();
        driver.printPath();
        distances = new float[]{3, 2, 4, 3, 5, 4, 6, 7, 7, 2, 4, 1.5f, 5};
        selfIntersecting = driver.move(distances);
        assertThat(selfIntersecting, is(true));

        driver = new SelfIntersectingPath();
        driver.printPath();
        distances = new float[]{3, 2, 4, 3, 5, 4, 6, 7, 7, 2, 4, 1.5f, 2, 1.5f};
        selfIntersecting = driver.move(distances);
        assertThat(selfIntersecting, is(true));

        driver = new SelfIntersectingPath();
        driver.printPath();
        distances = new float[]{3, 2, 4, 3, 5, 4, 6, 7, 7, 2, 4, 1.5f, 2, 1, 3};
        selfIntersecting = driver.move(distances);
        assertThat(selfIntersecting, is(true));

        driver = new SelfIntersectingPath();
        driver.printPath();
        distances = new float[]{3, 2, 4, 3, 5, 4, 6, 7, 7, 2, 4, 1.5f, 2, 1, 1.5f, 1};
        selfIntersecting = driver.move(distances);
        assertThat(selfIntersecting, is(true));
    }

    public boolean move(float[] distances) {
        boolean intersected = false;
        for (float distance : distances) {
            intersected = move(distance);
            printPath();
            if (intersected) {
                System.out.println("intersected->" + true);
                break;
            }
        }
        return intersected;
    }

    private boolean move(float distance) {
        Point pointB4Move = Point.create(travelerCoords[0], travelerCoords[1]);

        if (nextDirection == Direction.N) {
            travelerCoords[1] = pointB4Move.y + distance;
            Optional<Edge> newEdge = Optional.of(VerticalEdge.create(pointB4Move,
                    Point.create(travelerCoords[0], travelerCoords[1])));

            if (intersect(newEdge)) {
                return true;
            }
            verticalShadow = rightEdge;
            rightEdge = newEdge;
            nextDirection = Direction.W;
        } else if (nextDirection == Direction.W) {
            travelerCoords[0] = pointB4Move.x - distance;
            Optional<Edge> newEdge = Optional.of(HorizontalEdge.create(pointB4Move,
                    Point.create(travelerCoords[0], travelerCoords[1])));

            if (intersect(newEdge)) {
                return true;
            }
            horizontalShadow = upperEdge;
            upperEdge = newEdge;
            nextDirection = Direction.S;
        } else if (nextDirection == Direction.S) {
            travelerCoords[1] = pointB4Move.y - distance;
            Optional<Edge> newEdge = Optional.of(VerticalEdge.create(pointB4Move,
                    Point.create(travelerCoords[0], travelerCoords[1])));

            if (intersect(newEdge)) {
                return true;
            }
            verticalShadow = leftEdge;
            leftEdge = newEdge;
            nextDirection = Direction.E;
        } else if (nextDirection == Direction.E) {
            travelerCoords[0] = pointB4Move.x + distance;
            Optional<Edge> newEdge = Optional.of(HorizontalEdge.create(pointB4Move,
                    Point.create(travelerCoords[0], travelerCoords[1])));

            if (intersect(newEdge)) {
                return true;
            }
            horizontalShadow = bottomEdge;
            bottomEdge = newEdge;
            nextDirection = Direction.N;
        }
        return false;
    }

    private boolean intersect(Optional<Edge> edge) {
        return intersect(upperEdge, edge)
                || intersect(bottomEdge, edge)
                || intersect(horizontalShadow, edge)
                || intersect(rightEdge, edge)
                || intersect(leftEdge, edge)
                || intersect(verticalShadow, edge);
    }

    private boolean intersect(Optional<Edge> boundaryEdge, Optional<Edge> newEdge) {
        if (!boundaryEdge.isPresent() || !newEdge.isPresent()) {
            return false;
        }

        Optional<Point> intersection = boundaryEdge.get().intersect(newEdge);
        return intersection.isPresent() && !intersection.get().equals(newEdge.get().start);
    }

    private void printPath() {
        System.out.println("travelerCoords->" + Arrays.toString(travelerCoords));
        System.out.println("nextDirection->" + nextDirection);
        System.out.println("rightEdge->" + rightEdge);
        System.out.println("upperEdge->" + upperEdge);
        System.out.println("leftEdge->" + leftEdge);
        System.out.println("bottomEdge->" + bottomEdge);
        System.out.println("verticalShadow->" + verticalShadow);
        System.out.println("horizontalShadow->" + horizontalShadow);
        System.out.println("----------------------------------");
    }

    public enum Direction {N, S, E, W}

    private static class Point {
        private final float x, y;

        private Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public static Point create(float x, float y) {
            return new Point(x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (Float.compare(point.x, x) != 0) return false;
            return Float.compare(point.y, y) == 0;

        }

        @Override
        public int hashCode() {
            int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
            result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
            return result;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    private static abstract class Edge {
        protected final Point start, end;

        private Edge(Point start, Point end) {
            this.start = start;
            this.end = end;
        }

        public Optional<Point> intersect(Optional<Edge> other) {
            // intersection is meaning full for perpendicular edges only
            if (!other.isPresent() || this.getClass() == other.get().getClass()) {
                return Optional.empty();
            }

            Point intersection = intersectPoint(other.get());
            return onEdge(intersection) &&
                    other.get().onEdge(intersection)
                    ? Optional.of(intersection)
                    : Optional.empty();
        }

        protected abstract boolean onEdge(Point point);

        protected abstract Point intersectPoint(Edge edge);

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "[" + start + "#" + end + "]";
        }
    }

    private static class VerticalEdge extends Edge {
        private final float[] minMaxY = new float[2];

        private VerticalEdge(Point start, Point end) {
            super(start, end);
            minMaxY[0] = start.y;
            minMaxY[1] = end.y;
            Arrays.sort(minMaxY);
        }

        public static Edge create(Point start, Point end) {
            return new VerticalEdge(start, end);
        }

        @Override
        protected Point intersectPoint(Edge edge) {
            return Point.create(start.x, edge.start.y);
        }

        @Override
        protected boolean onEdge(Point point) {
            return point.x == start.x && point.y >= minMaxY[0] && point.y <= minMaxY[1];
        }
    }

    private static class HorizontalEdge extends Edge {
        private final float[] minMaxX = new float[2];

        private HorizontalEdge(Point start, Point end) {
            super(start, end);
            minMaxX[0] = start.x;
            minMaxX[1] = end.x;
            Arrays.sort(minMaxX);
        }

        public static Edge create(Point start, Point end) {
            return new HorizontalEdge(start, end);
        }

        @Override
        protected Point intersectPoint(Edge edge) {
            return Point.create(edge.start.x, start.y);
        }

        @Override
        protected boolean onEdge(Point point) {
            return point.y == start.y && point.x >= minMaxX[0] && point.x <= minMaxX[1];
        }
    }
}
