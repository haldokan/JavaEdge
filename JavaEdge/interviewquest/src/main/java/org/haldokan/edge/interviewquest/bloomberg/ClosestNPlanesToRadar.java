package org.haldokan.edge.interviewquest.bloomberg;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Bloomberg interview question - Easy kill! Using min heap. Though because scan can return the same
 * plane multiple times we have to find whether the plane exist in the heap before we can add it which degrade heap push
 * to O(n). Still heap is better than using a fully sorted data structure like a BST.
 * <p>
 * The Question: 4_STAR
 * <p>
 * Imagine an airport with the control tower having a constantly rotating radar scanning for airplanes. The radar's
 * coordinates in the 2-d plane are (0,0). The radar has an API: void scan(const Plane &P) that is called periodically
 * whenever the radar detects a plane. You can imagine that the Plane structure has x,y coordinates for that plane.
 * You should fill in the function Scan, such that at any given time you are able to return the 100 closest planes to
 * the tower (0,0).
 * <p>
 * Created by haytham.aldokanji on 8/5/16.
 */
public class ClosestNPlanesToRadar {
    private final Queue<Plane> minHeap;
    private final int topSampleSize;

    public ClosestNPlanesToRadar(int topSampleSize) {
        this.topSampleSize = topSampleSize;
        // suppose that radar can scan planes concurrently
        minHeap = new PriorityBlockingQueue<>(1024, (p1, p2) -> Double.valueOf(p1.distance).compareTo(p2.distance));
    }

    public static void main(String[] args) {
        int topSampleSize = 7;
        ClosestNPlanesToRadar driver = new ClosestNPlanesToRadar(topSampleSize);
        driver.test();
    }

    private void test() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            String planeId = "pid" + random.nextInt(20);
            double x = 100 + random.nextInt(100) + Math.random();
            double y = 120 + random.nextInt(100) + Math.random();

            scan(planeId, x, y);
        }

        List<Plane> closestPlanes = closestPlanes();
        System.out.printf("%s%n", closestPlanes);

        Plane[] sortedClosestPlanes = closestPlanes.stream().sorted((p1, p2) ->
                Double.valueOf(p1.distance).compareTo(p2.distance)).toArray(Plane[]::new);

        assertThat(closestPlanes.toArray(), is(sortedClosestPlanes));
    }

    public void scan(String id, double x, double y) {
        addToHeap(new Plane(id, x, y));
    }

    public List<Plane> closestPlanes() {
        Queue<Plane> snapshot = new PriorityQueue<>((p1, p2) -> Double.valueOf(p1.distance).compareTo(p2.distance));
        snapshot.addAll(minHeap);

        List<Plane> result = new ArrayList<>();
        Plane plane;
        do {
            plane = snapshot.poll();
            if (plane != null) {
                result.add(plane);
            }
        } while (plane != null);

        return result;
    }

    public void addToHeap(Plane plane) {
        Optional<Plane> potentialPlane = minHeap.stream()
                .filter(p -> p.id.equals(plane.id))
                .findAny();

        if (potentialPlane.isPresent()) {
            Plane planeAlreadyInHeap = potentialPlane.get();
            minHeap.remove(planeAlreadyInHeap);
            minHeap.add(plane);
        } else if (minHeap.size() < topSampleSize || plane.distance > minHeap.peek().distance) {
            minHeap.add(plane);
            if (minHeap.size() > topSampleSize) {
                minHeap.remove();
            }
        }
    }

    private static class Plane {
        private final String id;
        private final double x, y;
        private final double distance;

        public Plane(String id, double x, double y) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.distance = Math.sqrt(x * x + y * y);
        }

        @Override
        public String toString() {
            return "Plane{" +
                    "id='" + id + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    ", distance=" + distance +
                    '}' + "\n";
        }
    }
}
