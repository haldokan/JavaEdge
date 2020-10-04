package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.Lists;

import java.util.*;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question
 *
 * The Question: 4.5-STAR
 *
 * Given a list of rectangles, pick a random point within one of the rectangles, where the likelihood of a point
 * is based on the area of the rectangle.
 *
 * 10/4/20
 */
public class RandomPointInListOfRectangles {
    private final Map<double[], Rectangle> rectangleByProbabilityRange;
    private final double[][] ranges;

    public static void main(String[] args) {
        List<Rectangle> rectangles = Lists.newArrayList(
                new Rectangle(1.2, 1.9, 3.5, 2.1),
                new Rectangle(1.7, 2.9, 4.5, 3.1),
                new Rectangle(2.2, 3.9, 8.5, 5.1));

        RandomPointInListOfRectangles driver = new RandomPointInListOfRectangles(rectangles);
        driver.testRectangleRandomPoint();
        driver.testRandomPoint();
    }

    public RandomPointInListOfRectangles(List<Rectangle> rectangles) {
        this.rectangleByProbabilityRange = mapProbabilityRange(rectangles);
        this.ranges = this.rectangleByProbabilityRange.keySet().toArray(double[][]::new);
    }

    public double[] randomPoint() {
        double[] probabilityRange = ranges[rangeIndex(Math.random(), 0, ranges.length)];
        return rectangleByProbabilityRange.get(probabilityRange).randomPoint();
    }

    private Map<double[], Rectangle> mapProbabilityRange(List<Rectangle> rectangles) {
        double totalArea = rectangles.stream().mapToDouble(rectangle -> rectangle.length * rectangle.height).sum();
        Map<double[], Rectangle> rectangleByRange = new TreeMap<>(Comparator.comparingDouble(range -> range[1]));

        double boundary = 0;
        for (Rectangle rectangle : rectangles) {
            double highBoundary = rectangle.area() / totalArea;
            rectangleByRange.put(new double[]{boundary, boundary + highBoundary}, rectangle);
            boundary += highBoundary;
        }
        return rectangleByRange;
    }

    // we use binary-search since ranges are ordered (key-set of TreeMap)
    private int rangeIndex(double value, int start, int end) {
        if (start >= end) {
            return -start;
        }
        int mid = (start + end) / 2;
        double[] range = ranges[mid];

        if (value >= range[0] && value  < range[1]) {
            return mid;
        }
        return value < range[0] ? rangeIndex(value, start, mid) : rangeIndex(value, mid + 1, end);
    }

    private static final class Rectangle {
        private final double x;
        private final double y;
        private final double length;
        private final double height;

        private Rectangle(double x, double y, double length, double height) {
            this.x = x;
            this.y = y;
            this.length = length;
            this.height = height;
        }

        private double[] randomPoint() {
            return new double[]{x + Math.random() * x, y + Math.random() * y};
        }

        private double area() {
            return length * height;
        }

        @Override
        public String toString() {
            return String.format("%f|%f|%f|%f%n", x, y, length, height);
        }
    }

    private void testRectangleRandomPoint() {
        Random random = new Random();
        int randomLength = 1 + random.nextInt(1000);
        int randomHeight = 1 + random.nextInt(500);

        for (int i = 0; i < 100; i++) {
            Rectangle rectangle = new Rectangle(1.2, 2.3, randomLength, randomHeight);
            double[] randomPoint = rectangle.randomPoint();

            assertThat(randomPoint[0], lessThan(rectangle.x + rectangle.length));
            assertThat(randomPoint[1], lessThan(rectangle.y + rectangle.height));
        }
    }

    private void testRandomPoint() {
        // lame test: eyeball to see that most of the random points are in the larger rectangles
        for (int i = 0; i < 10; i++) {
            System.out.println(Arrays.toString(randomPoint()));
        }
    }
}
