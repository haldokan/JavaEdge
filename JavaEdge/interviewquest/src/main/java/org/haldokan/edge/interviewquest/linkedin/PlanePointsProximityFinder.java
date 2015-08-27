package org.haldokan.edge.interviewquest.linkedin;

import java.util.*;

/**
 * My solution to a Linkedin interview question. Description is in method comments (1st 2 methods). Solution distinguish
 * b/w existing centers and non-existing centers assuming we want the calculation to be done quickly for existing
 * centers. Solution can be simplified if we do not store the calculated distances (for efficiency).
 * <p>
 * Solution can be made nicer using Guava Table data structure
 *
 * @author haldokan
 */
public class PlanePointsProximityFinder {
    private Map<Point, Set<EdgePoint>> plane = new HashMap<>();

    public static void main(String[] args) {
        PlanePointsProximityFinder finder = new PlanePointsProximityFinder();
        finder.addPoint(new Point(0d, 1d));
        finder.addPoint(new Point(1.2d, 10.3d));
        finder.addPoint(new Point(1d, 20d));
        finder.addPoint(new Point(2d, 70d));
        finder.addPoint(new Point(3.7d, 10.3d));
        finder.addPoint(new Point(0d, 3d));

        // stored point
        System.out.println(finder.findNearest(new Point(0d, 1d), 3));
        // point not stored
        System.out.println(finder.findNearest(new Point(0d, 0d), 3));
    }

    /**
     * Stores a given point in an internal data structure
     */
    public void addPoint(Point newPoint) {
        if (plane.containsKey(newPoint))
            throw new IllegalArgumentException("Point is already defined in plane");
        addNewPointToExistingPointSet(newPoint);
        addExistingPointsToNewPointSet(newPoint);
    }

    /**
     * For given 'center' point returns a subset of of size 'm' of stored points that are closer to the center than
     * others.
     * <p>
     * E.g. Stored: (0, 1) (0, 2) (0, 3) (0, 4) (0, 5)
     * <p>
     * findNearest(new Point(0, 0), 3) -> (0, 1), (0, 2), (0, 3)
     */
    public Collection<Point> findNearest(Point center, int m) {
        Set<EdgePoint> edges = plane.get(center);
        // TODO these 2 find methods can use some re-factoring to extract common steps b/w the 2
        if (edges == null)
            return findClosePointsToNonStoredPoint(center, m);
        else
            return findClosePointsToStoredPoint(m, edges);
    }

    private Collection<Point> findClosePointsToStoredPoint(int m, Set<EdgePoint> edges) {
        Set<Point> closestPoints = new LinkedHashSet<>();
        if (edges != null) {
            int index = 0;
            for (EdgePoint edge : edges) {
                if (index < m) {
                    closestPoints.add(edge.point);
                    index++;
                } else {
                    break;
                }
            }
        }
        return closestPoints;
    }

    private Set<Point> findClosePointsToNonStoredPoint(Point newPoint, int m) {
        Set<Point> closePoints = new LinkedHashSet<>();
        Set<EdgePoint> edges = new TreeSet<>();
        for (Point storedPoint : plane.keySet()) {
            EdgePoint edgePoint = new EdgePoint(storedPoint, distanceBwPoints(newPoint, storedPoint));
            edges.add(edgePoint);
        }
        int index = 0;
        for (EdgePoint edge : edges) {
            if (index < m) {
                closePoints.add(edge.point);
                index++;
            } else {
                break;
            }
        }
        return closePoints;
    }

    private void addNewPointToExistingPointSet(Point newPoint) {
        for (Map.Entry<Point, Set<EdgePoint>> storedPoint : plane.entrySet()) {
            EdgePoint edgePoint = new EdgePoint(newPoint, distanceBwPoints(newPoint, storedPoint.getKey()));
            storedPoint.getValue().add(edgePoint);
        }
    }

    private void addExistingPointsToNewPointSet(Point newPoint) {
        plane.put(newPoint, new TreeSet<EdgePoint>());
        for (Point storedPoint : plane.keySet()) {
            if (!storedPoint.equals(newPoint)) {
                EdgePoint edgePoint = new EdgePoint(storedPoint, distanceBwPoints(newPoint, storedPoint));
                plane.get(newPoint).add(edgePoint);
            }
        }
    }

    private double distanceBwPoints(Point newPoint, Point storedPoint) {
        return Math.sqrt(Math.pow((newPoint.x - storedPoint.x), 2) + Math.pow((newPoint.y - storedPoint.y), 2));
    }

    private static class EdgePoint implements Comparable<EdgePoint> {
        private Point point;
        private Double dist;

        public EdgePoint(Point point, Double dist) {
            this.point = point;
            this.dist = dist;
        }

        @Override
        public int compareTo(EdgePoint o) {
            return dist.compareTo(o.dist);
        }
    }

    private static class Point {
        private Double x, y;

        public Point(Double x, Double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            long temp;
            temp = Double.doubleToLongBits(x);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(y);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Point other = (Point) obj;
            if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
                return false;
            if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}
