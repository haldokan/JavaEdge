package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.Sets;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * My solution to an Amazon interview question
 *
 * The Question: 4_STAR
 * <p>
 * Write code for scheduling algorithms for a cab service provided you have a list of future bookings, and list
 * of cabs in your fleet.
 * <p>
 * Created by haytham.aldokanji on 7/28/16.
 */
public class CarServiceBookingScheduler2 {
    Set<String> cabs;
    List<List<String>> bookings = new ArrayList<>();

    public static void main(String[] args) {
        Set<String> cabs = Sets.newHashSet("cab1", "cab2", "cab3");
        CarServiceBookingScheduler2 driver = new CarServiceBookingScheduler2(cabs);
        driver.test();
    }

    void test() {
        System.out.println(book(LocalDate.now(), LocalDate.now().plusDays(3)));
        System.out.println(book(LocalDate.now(), LocalDate.now().plusDays(3)));

        Booking booking = book(LocalDate.now(), LocalDate.now().plusDays(3));
        System.out.println(booking);

        unbook(booking);
        System.out.println(book(LocalDate.now(), LocalDate.now().plusDays(3)));
    }

    public CarServiceBookingScheduler2(Set<String> cabs) {
        this.cabs = cabs;
        for (int i = 1; i < 365; i++) {
            bookings.add(new ArrayList<>());
        }
    }

    Booking book(LocalDate start, LocalDate end) {
        int startDay = start.getDayOfYear();
        int endDay = end.getDayOfYear();

        Set<String> bookedCars = new HashSet<>();
        for (int i = startDay; i <= endDay; i++) {
            Collections.addAll(bookedCars, bookings.get(i).toArray(new String[]{}));
        }
        Set<String> availableCars = Sets.difference(cabs, bookedCars);
        if (!availableCars.isEmpty()) {
            String car = availableCars.iterator().next();
            for (int i = startDay; i <= endDay; i++) {
                bookings.get(i).add(car);
            }
            return new Booking(car, start, end);
        }
        return null;
    }

    void unbook(Booking booking) {
        for (int i = booking.start.getDayOfYear(); i <= booking.end.getDayOfYear(); i++) {
            bookings.get(i).remove(booking.car);
        }
    }

    static class Booking {
        LocalDate start;
        LocalDate end;
        String car;

        public Booking(String car, LocalDate start, LocalDate end) {
            this.car = car;
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Booking booking = (Booking) o;
            return car.equals(booking.car);
        }

        @Override
        public int hashCode() {
            return Objects.hash(car);
        }

        @Override
        public String toString() {
            return "Bookings{" +
                "start=" + start +
                ", end=" + end +
                ", car='" + car + '\'' +
                '}';
        }
    }
}
