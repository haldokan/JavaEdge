package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.ImmutableListMultimap;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to an Amazon interview question - Designed the data structures in a way that reserving a parking lot that
 * optimally fits the car is done in O(1). Canceling or finding a reservation is done also in O(1). The constant time
 * complexity is achieved by using the ParkingGround data structure that maintains 2 sets of parking lots: occupied and
 * free. reserving or cancelling the reservation of a parking lot involves moving the lot b/w these 2 sets which is an
 * O(1) operation.
 * Finding a parking lot based on it's id (an operation that is uncommon) is done in O(n) where n is the number of
 * parking lots (it does not warrant an optimization that will required more space complexity). The solution space
 * complexity is O(n) in the number of parking lots.
 * <p>
 * Note: the question can be made more interesting (and much harder) by adding the concept of parking intervals
 * and by asking for parking optimization as new parking lots become available at the intervals' expiry times. For example
 * when there are 2 lots one small and the another is large. The small lot is free and the large lot is occupied by a
 * small car. Now we have to reserve a lot for a large car. The reservation is possible only when we re-assign the small
 * car to the small lot and thus freeing the large lot for the large car.
 * I think a dynamic-programming based solution is possible for this much harder (Google-type) problem.
 * <p>
 * The Question: 5_STAR (booking, cancelling or finding a reservation is done in O(1), hence the 5 stars).
 * <p>
 * Design a valet parking system. Requirements of the valet parking system should be:
 * 1. Customer are given a ticket that they can use to get their vehicle back
 * 2. Parking spots come in three sizes, small, med, large
 * 3. Thee types of vehicles, small, med, large
 * a. Small vehicle can park in a small, medium, and large spot
 * b. Medium vehicle can park in a medium and large spot
 * c. Large vehicle can park in a large spot
 * <p>
 * Created by haytham.aldokanji on 7/29/16.
 */
public class ValetParkingSystem {
    private static final IdGenerator idGenerator = new IdGenerator();
    private static final ImmutableListMultimap<SizeType, SizeType> interchangeableSizeTypes =
            new ImmutableListMultimap.Builder<SizeType, SizeType>()
                    .putAll(SizeType.SMALL, SizeType.SMALL, SizeType.MEDIUM, SizeType.LARGE)
                    .putAll(SizeType.MEDIUM, SizeType.MEDIUM, SizeType.LARGE)
                    .put(SizeType.LARGE, SizeType.LARGE)
                    .build();
    private final Map<SizeType, ParkingSegment> parkingGround = new HashMap<>();
    // I am assuming that the ticket id is not the same as the lot id. Adding this level of in-direction allows the system
    // to re-assign cars to different parking lots as more fitting lot sizes become available and thus increasing the parking
    // ground capacity to park larger cars
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        ValetParkingSystem driver = new ValetParkingSystem();
        driver.test();
    }

    // in a real app tests should be specialized
    private void test() {
        int numberOfLots = 10;
        // init parking ground: In a real app this correspond to loading the parking lots from db and caching them
        IntStream.range(0, 5).forEach(i -> {
            addParkingLot(ParkingLot.create("smallLot" + i, SizeType.SMALL));
            assertThat(getParkingLot("smallLot" + i).get().getId(), is("smallLot" + i));
        });

        addParkingLot(ParkingLot.create("mediumLot1", SizeType.MEDIUM));
        assertThat(getParkingLot("mediumLot1").get().getId(), is("mediumLot1"));

        addParkingLot(ParkingLot.create("mediumLot2", SizeType.MEDIUM));
        assertThat(getParkingLot("mediumLot2").get().getId(), is("mediumLot2"));

        addParkingLot(ParkingLot.create("mediumLot3", SizeType.MEDIUM));
        assertThat(getParkingLot("mediumLot3").get().getId(), is("mediumLot3"));

        addParkingLot(ParkingLot.create("largeLot1", SizeType.LARGE));
        assertThat(getParkingLot("largeLot1").get().getId(), is("largeLot1"));

        addParkingLot(ParkingLot.create("largeLot2", SizeType.LARGE));
        assertThat(getParkingLot("largeLot2").get().getId(), is("largeLot2"));

        Car smallCar = new Car("smallCar", SizeType.SMALL);
        Car mediumCar = new Car("mediumCar", SizeType.MEDIUM);
        Car largeCar = new Car("largeCar", SizeType.LARGE);

        // small cars
        String[] tickets = new String[numberOfLots];
        IntStream.range(0, 12).forEach(i -> {
            Optional<String> ticket = reserveParkingLot(smallCar);
            if (i < numberOfLots) {
                assertThat(ticket.isPresent(), is(true));
                tickets[i] = ticket.get();
            } else {
                assertThat(ticket.isPresent(), is(false));
            }
        });
        Arrays.stream(tickets).forEach(this::cancelReservation);
        Arrays.fill(tickets, null);

        // medium cars
        IntStream.range(0, numberOfLots + 2).forEach(i -> {
            Optional<String> ticket = reserveParkingLot(mediumCar);
            if (i < numberOfLots / 2) {
                assertThat(ticket.isPresent(), is(true));
                tickets[i] = ticket.get();
            } else {
                assertThat(ticket.isPresent(), is(false));
            }
        });
        Arrays.stream(tickets).filter(t -> t != null).forEach(this::cancelReservation);
        Arrays.fill(tickets, null);

        // large cars
        IntStream.range(0, 4).forEach(i -> {
            Optional<String> ticket = reserveParkingLot(largeCar);
            if (i < 2) {
                assertThat(ticket.isPresent(), is(true));
                tickets[i] = ticket.get();
            } else {
                assertThat(ticket.isPresent(), is(false));
            }
        });
        Arrays.stream(tickets).filter(t -> t != null).forEach(this::cancelReservation);
        Arrays.fill(tickets, null);
        //===========================
        // small cars again
        IntStream.range(0, 7).forEach(i -> {
            Optional<String> ticket = reserveParkingLot(smallCar);
            assertThat(ticket.isPresent(), is(true));
            tickets[i] = ticket.get();
        });
        // 1 medium and 2 large left
        IntStream.range(7, 10).forEach(i -> {
            Optional<String> ticket = reserveParkingLot(mediumCar);
            assertThat(ticket.isPresent(), is(true));
            tickets[i] = ticket.get();
        });
        // we won't be able to make any more reservations
        assertThat(reserveParkingLot(largeCar).isPresent(), is(false));
        // remove a small lot reservation: won't be able to reserve a medium or large lot
        cancelReservation(tickets[0]);
        assertThat(reserveParkingLot(mediumCar).isPresent(), is(false));
        assertThat(reserveParkingLot(largeCar).isPresent(), is(false));
        // only a small lot can be reserved
        Optional<String> ticket = reserveParkingLot(smallCar);
        assertThat(ticket.isPresent(), is(true));
        tickets[0] = ticket.get();

        // remove a medium lot reservation: won't be able to reserve a large lot
        cancelReservation(tickets[7]);
        assertThat(reserveParkingLot(largeCar).isPresent(), is(false));
        // only a medium (or small) lot can be reserved
        ticket = reserveParkingLot(mediumCar);
        assertThat(ticket.isPresent(), is(true));
        tickets[7] = ticket.get();

        // remove a large lot reservation
        cancelReservation(tickets[9]);
        // now we can reserve a large lot
        ticket = reserveParkingLot(largeCar);
        assertThat(ticket.isPresent(), is(true));
        tickets[9] = ticket.get();
    }

    public void addParkingLot(ParkingLot parkingLot) {
        ParkingSegment parkingSegment = parkingGround.computeIfAbsent(parkingLot.getSizeType(),
                v -> ParkingSegment.create(parkingLot.getSizeType()));

        parkingSegment.addParkingLot(parkingLot);
    }

    public Optional<String> reserveParkingLot(Car car) {
        List<SizeType> fittingTypes = interchangeableSizeTypes.get(car.getSizeType());

        for (SizeType sizeType : fittingTypes) {
            ParkingSegment parkingSegment = parkingGround.get(sizeType);
            Optional<ParkingLot> parkingLot = parkingSegment.reserveParkingLot();

            if (parkingLot.isPresent()) {
                String ticket = idGenerator.getId();
                Reservation reservation = Reservation.create(ticket, car, parkingLot.get());
                reservations.put(ticket, reservation);
                return Optional.of(ticket);
            }
        }
        return Optional.empty();
    }

    public void cancelReservation(String ticket) {
        Reservation reservation = reservations.get(ticket);
        ParkingLot parkingLot = reservation.getParkingLot();
        parkingGround.get(parkingLot.sizeType).freeParkingLot(parkingLot);
        reservations.remove(ticket);
    }

    public Set<ParkingLot> getFreeParkingLots(SizeType sizeType) {
        return parkingGround.get(sizeType).getFreeParkingLots();
    }

    public Set<ParkingLot> getOccupiedParkingLots(SizeType sizeType) {
        return parkingGround.get(sizeType).getOccupiedParkingLots();
    }

    public Optional<ParkingLot> getParkingLot(String parkingLotId) {
        for (SizeType sizeType : SizeType.values()) {
            Optional<ParkingLot> parkingLot = getParkingLot(parkingLotId, sizeType);

            if (parkingLot.isPresent()) {
                return parkingLot;
            }
        }
        return Optional.empty();
    }

    public Optional<ParkingLot> getParkingLot(String parkingLotId, SizeType sizeType) {
        return parkingGround.get(sizeType).getParkingLot(parkingLotId);
    }

    private enum SizeType {SMALL, MEDIUM, LARGE}

    private static final class ParkingLot {
        private final String id;
        private final SizeType sizeType;

        private ParkingLot(String id, SizeType sizeType) {
            this.id = id;
            this.sizeType = sizeType;
        }

        public static ParkingLot create(String id, SizeType sizeType) {
            return new ParkingLot(id, sizeType);
        }

        public String getId() {
            return id;
        }

        public SizeType getSizeType() {
            return sizeType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ParkingLot that = (ParkingLot) o;
            return id.equals(that.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }

    private static final class Car {
        private final String id;
        private final SizeType sizeType;

        public Car(String id, SizeType sizeType) {
            this.id = id;
            this.sizeType = sizeType;
        }

        public SizeType getSizeType() {
            return sizeType;
        }
    }

    private static final class ParkingSegment {
        private final SizeType sizeType;
        private final Set<ParkingLot> free = new HashSet<>();
        private final Set<ParkingLot> occupied = new HashSet<>();

        private ParkingSegment(SizeType sizeType) {
            this.sizeType = sizeType;
        }

        public static ParkingSegment create(SizeType sizeType) {
            return new ParkingSegment(sizeType);
        }

        // adding and removing lots to respective sets must be transactional so synchronization is needed
        public synchronized Optional<ParkingLot> reserveParkingLot() {
            if (free.isEmpty()) {
                return Optional.empty();
            }
            ParkingLot lot = free.iterator().next();
            free.remove(lot);
            occupied.add(lot);

            return Optional.of(lot);
        }

        public synchronized void freeParkingLot(ParkingLot parkingLot) {
            occupied.remove(parkingLot);
            free.add(parkingLot);
        }

        public Set<ParkingLot> getFreeParkingLots() {
            // using Guava ImmutableSet.copyOf will deep-clone the ParkingLot's. We don't want that since ParkingLot is
            // immutable and we can save on copying
            return Collections.unmodifiableSet(free);
        }

        public Set<ParkingLot> getOccupiedParkingLots() {
            return Collections.unmodifiableSet(occupied);
        }

        // finding a parking lot based on its id (uncommon operation in the system) is O(n) in the number of parking lots
        public Optional<ParkingLot> getParkingLot(String lotId) {
            Optional<ParkingLot> parkingLot = free.stream().filter(lot -> lot.getId().equals(lotId)).findAny();
            if (!parkingLot.isPresent()) {
                parkingLot = occupied.stream().filter(lot -> lot.getId().equals(lotId)).findAny();
            }
            return parkingLot;
        }

        public void addParkingLot(ParkingLot parkingLot) {
            free.add(parkingLot);
        }
    }

    private static final class Reservation {
        private final String ticket;
        private final Car car;
        private ParkingLot parkingLot;

        private Reservation(String ticket, Car car, ParkingLot parkingLot) {
            this.ticket = ticket;
            this.car = car;
            this.parkingLot = parkingLot;
        }

        public static Reservation create(String ticket, Car car, ParkingLot parkingLot) {
            return new Reservation(ticket, car, parkingLot);
        }

        public String getTicket() {
            return ticket;
        }

        public Car getCar() {
            return car;
        }

        public ParkingLot getParkingLot() {
            return parkingLot;
        }
    }

    public static class IdGenerator {
        private SecureRandom random = new SecureRandom();

        public String getId() {
            return new BigInteger(128, random).toString(32);
        }
    }
}
