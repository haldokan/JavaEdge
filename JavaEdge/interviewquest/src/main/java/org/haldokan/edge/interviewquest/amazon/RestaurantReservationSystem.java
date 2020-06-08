package org.haldokan.edge.interviewquest.amazon;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * My solution to an Amazon interview question - Not just modeled but implemented too
 * The Question: 4_STAR
 * <p>
 * Model a restaurant reservation system, where staff can book, find or cancel a reservation. The reservation
 * system is very simple local to just one terminal at the restaurant not connected to network
 * <p>
 * Created by haytham.aldokanji on 7/25/16.
 */
public class RestaurantReservationSystem {
    private final static IdGenerator idGenerator = new IdGenerator();
    private final Map<String, Table> tables = new ConcurrentHashMap<>();
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();
    private final int bailoutWaitMinutes = 15; // should be passed in constructor or sth
    private final int reservationMonitoringDelayMinutes = 1; // should be passed in constructor or sth

    public boolean makeReservation(String customerName,
                                   String phoneNumber,
                                   int numberOfGuests,
                                   LocalDateTime startTime) {
        Optional<Table> potentialTable = bookTable(numberOfGuests);

        if (potentialTable.isPresent()) {
            Reservation reservation = new Reservation(
                    idGenerator.getId(),
                    customerName,
                    phoneNumber,
                    potentialTable.get(),
                    startTime,
                    calculateEndTime(numberOfGuests, startTime)
            );

            reservations.put(phoneNumber, reservation);
            return true;
        }
        return false;
    }

    public Optional<Reservation> findReservation(String phoneNumber) {
        return Optional.ofNullable(reservations.get(phoneNumber));
    }

    // mark reservation as active so that it is not cancelled by the monitor while customer is still having dinner!
    public boolean customerCheckedIn(String phoneNumber) {
        Optional<Reservation> reservation = findReservation(phoneNumber);
        if (reservation.isPresent()) {
            reservation.get().customerCheckedIn = true;
            // reservation is active now
            return true;
        }
        // checked in too late; their table might have been re-booked and they need to reserve again if possible
        return false;
    }

    public void cancelReservation(String phoneNumber) {
        Reservation reservation = reservations.remove(phoneNumber);
        if (reservation != null) {
            unbookTable(reservation.table.id);
        }
    }

    // cancel reservation if customer is too late or their allotted time slot has expired
    public void runReservationMonitor() {
        ScheduledExecutorService monitor = Executors.newSingleThreadScheduledExecutor();
        LocalDateTime now = LocalDateTime.now();
        monitor.scheduleWithFixedDelay(() -> {
            String[] expiredReservations = reservations.values().stream()
                    .filter(r -> r.endTime.isBefore(now) ||
                            (r.startTime.plusMinutes(bailoutWaitMinutes).isBefore(now) && !r.customerCheckedIn))
                    .map(r -> r.phoneNumber)
                    .toArray(String[]::new);

            for (String phoneNumber : expiredReservations) {
                cancelReservation(phoneNumber);
            }
        }, 0, reservationMonitoringDelayMinutes, TimeUnit.MINUTES);
    }

    private LocalDateTime calculateEndTime(int numOfGuests, LocalDateTime time) {
        // can be elaborate logic
        return time.plusMinutes(60 + numOfGuests * 5);
    }

    // gets a table that best matches the number of guests; sorting and getting first matching table insures best fit
    private Optional<Table> bookTable(int numberOfGuests) {
        Optional<Table> table = tables.values().stream()
            .filter(t -> !t.reserved)
            .filter(t -> t.numberOfSeats >= numberOfGuests).min(Comparator.comparingInt(t -> t.numberOfSeats));

        table.ifPresent(value -> value.reserved = true);
        return table;
    }

    public void unbookTable(String id) {
        Table table = tables.get(id);
        if (table != null) {
            table.reserved = false;
        }
    }

    private static class Table {
        private final String id;
        private final int numberOfSeats;
        private boolean reserved;

        public Table(String id, int numberOfSeats) {
            this.id = id;
            this.numberOfSeats = numberOfSeats;
        }
    }

    private static class Reservation {
        private final String id;
        private final String customer;
        private final String phoneNumber;
        private final Table table;
        private int numberOfGuests;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private boolean customerCheckedIn;

        public Reservation(String id,
                           String customer,
                           String phoneNumber,
                           Table table,
                           LocalDateTime startTime,
                           LocalDateTime endTime) {
            this.id = id;
            this.customer = customer;
            this.phoneNumber = phoneNumber;
            this.table = table;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    public static class IdGenerator {
        private SecureRandom random = new SecureRandom();

        public String getId() {
            return new BigInteger(130, random).toString(32);
        }
    }
}
