package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.ListMultimap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to an Amazon interview question - I implemented an interval tree so finding out whether a booking interval
 * intersects with any other already booked intervals is done in O(log n) instead of O(n). Finding a customer's last booking
 * is done in O(1) using a mapping b/w customer and their bookings.
 * NOTE: another way (maybe simpler) to do it is in CarServiceBookingScheduler2
 * <p>
 * The idea is to consult the interval tree at each booking and find all the intervals that intersect with the new booking
 * interval then subtract the sum of the booked cars for all the intersecting intervals from the fleet size.
 * <p>
 * The Question: 5_STAR
 * <p>
 * Write code for scheduling algorithms for a cab service provided you have a list of future bookings, and list
 * of cabs in your fleet.
 * <p>
 * Created by haytham.aldokanji on 7/28/16.
 */
public class CarServiceBookingScheduler {
    private final int fleetSize;
    private final IntervalTree<LocalDateTime> schedule;
    private final ListMultimap<String, Booking> bookings;

    public CarServiceBookingScheduler(int fleetSize) {
        this.fleetSize = fleetSize;
        schedule = new IntervalTree<>();
        bookings = ArrayListMultimap.create();
    }

    public static void main(String[] args) {
//        test1();
        test2();
//        test3();
    }

    private static void test1() {
        CarServiceBookingScheduler scheduler = new CarServiceBookingScheduler(10);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(10);

        assertThat(scheduler.book(start, end, "customer1", 5), is(true));
        assertThat(scheduler.book(start, end, "customer1", 3), is(true));
        assertThat(scheduler.book(start, end, "customer1", 2), is(true));
        assertThat(scheduler.book(start, end, "customer1", 1), is(false));
    }

    private static void test2() {
        CarServiceBookingScheduler scheduler = new CarServiceBookingScheduler(10);
        LocalDateTime start = LocalDateTime.now();

        assertThat(scheduler.book(start, start.plusHours(5), "customer1", 5), is(true));
        assertThat(scheduler.book(start, start.plusHours(4), "customer1", 5), is(true));
        assertThat(scheduler.book(start.plusHours(6), start.plusHours(8), "customer1", 8), is(true));
        assertThat(scheduler.book(start.plusHours(8), start.plusHours(9), "customer1", 2), is(true));
        assertThat(scheduler.book(start.plusHours(10), start.plusHours(11), "customer1", 9), is(true));
        assertThat(scheduler.book(start.plusHours(12), start.plusHours(13), "customer1", 9), is(true));
        assertThat(scheduler.book(start.plusHours(12), start.plusHours(13), "customer1", 1), is(true));
        assertThat(scheduler.book(start.plusHours(12), start.plusHours(13), "customer1", 1), is(false));
    }

    private static void test3() {
        CarServiceBookingScheduler scheduler = new CarServiceBookingScheduler(10);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(10);

        assertThat(scheduler.book(start, end, "customer1", 5), is(true));
        assertThat(scheduler.getBooking("customer1").get().numberOfCars, is(5));

        assertThat(scheduler.book(start, end, "customer1", 3), is(true));
        assertThat(scheduler.getBooking("customer1").get().numberOfCars, is(3));

        assertThat(scheduler.book(start, end, "customer1", 2), is(true));
        assertThat(scheduler.getBooking("customer1").get().numberOfCars, is(2));

        assertThat(scheduler.book(start, end, "customer1", 1), is(false));
        assertThat(scheduler.getBooking("customer1").get().numberOfCars, is(2));

        assertThat(scheduler.getBookings("customer1").size(), is(3));
    }

    public boolean book(LocalDateTime start, LocalDateTime end, String customer, int numberOfRequestedCars) {
        Booking<LocalDateTime> booking = new Booking<>(start, end, customer, numberOfRequestedCars);

        List<Node<LocalDateTime>> overlapping = new ArrayList<>();
        schedule.findOverlapping(booking, overlapping);

        int availableCars = fleetSize - overlapping.stream().mapToInt(node -> ((Booking) node.interval).numberOfCars()).sum();

        if (availableCars >= numberOfRequestedCars) {
            schedule.insert(booking);
            bookings.put(customer, booking);
            return true;
        }
        return false;
    }

    // this can be alternatively based on a booking id that is generated by the booking process
    public Optional<Booking> getBooking(String customer) {
        List<Booking> customerBookings = bookings.get(customer);
        // since a customer bookings are naturally sorted by time (or order of entry) we can simply access the last booking
        if (customerBookings != null && !customerBookings.isEmpty()) {
            return Optional.of(Iterators.getLast(customerBookings.iterator()).copy());
        }
        return Optional.empty();
    }

    public List<Booking> getBookings(String customer) {
        List<Booking> customerBookings = bookings.get(customer);
        if (customerBookings != null) {
            return ImmutableList.copyOf(customerBookings);
        }
        return Collections.emptyList();
    }

    private interface Interval<T extends Comparable<? super T>> {
        T start();

        T end();

        T max();

        void setMax(T max);
    }

    private static class Booking<T extends Comparable<? super T>> implements Interval<T> {
        private final T start, end;
        private final int numberOfCars;
        private final String customer;
        private T max;

        public Booking(T start, T end, String customer, int numberOfCars) {
            this.start = start;
            this.end = end;
            this.max = end;
            this.customer = customer;
            this.numberOfCars = numberOfCars;
        }

        public Booking<T> copy() {
            return new Booking<>(start, end, customer, numberOfCars);
        }

        @Override
        public T start() {
            return start;
        }

        @Override
        public T end() {
            return end;
        }

        @Override
        public T max() {
            return max;
        }

        @Override
        public void setMax(T max) {
            this.max = max;
        }

        public int numberOfCars() {
            return numberOfCars;
        }
    }

    public static class IntervalTree<T extends Comparable<? super T>> {
        private static final int COUNT = 10;
        private Node<T> node;

        private void insert(Interval<T> interval) {
            if (node == null) {
                interval.setMax(interval.end());
                node = new Node<>(interval);
            } else {
                insert(node, interval);
            }
            printTree(node, 15);
            System.out.println("--------------");
        }

        private Node<T> insert(Node<T> node, Interval<T> interval) {
            if (node == null) {
                return new Node<>(interval);
            }

            if (interval.start().compareTo(node.interval.start()) < 0) {
                node.left = insert(node.left, interval);
            } else {
                node.right = insert(node.right, interval);
            }
            // we can ask any node for the max range since it is set on all of them
            if (node.interval.max().compareTo(interval.max()) < 0) {
                node.interval.setMax(interval.max());
            }
            return node;
        }

        public void findOverlapping(Interval<T> interval, List<Node<T>> overlapping) {
            findOverlapping(node, interval, overlapping);
        }

        private void findOverlapping(Node<T> node, Interval<T> interval, List<Node<T>> overlapping) {
            if (node == null) {
                return;
            }
            if (interval.start().compareTo(node.interval.end()) <= 0
                && node.interval.start().compareTo(interval.end()) <= 0) {
                overlapping.add(node);
            }
            // optimization using the max range: If the max range on the left is less than the interval start then no point in descending left
            if (node.left != null && node.left.interval.max().compareTo(interval.start()) >= 0) {
                findOverlapping(node.left, interval, overlapping);
            }
            findOverlapping(node.right, interval, overlapping);
        }

        // borrowed from some code I found online
        public void printTree(Node root, int space) {
            if (root == null) {
                return;
            }

            space += COUNT;
            printTree(root.right, space);

            System.out.print("\n");
            for (int i = COUNT; i < space; i++) {
                System.out.print(" ");
            }
            System.out.print(root.interval.max() + "\n");
            printTree(root.left, space);
        }
    }

    private static class Node<T extends Comparable<? super T>> {
        private final Interval<T> interval;
        private Node<T> left, right;

        public Node(Interval<T> interval) {
            this.interval = interval;
        }

        @Override
        public String toString() {
            return interval.toString();
        }
    }

}
