package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - solution is generalized to account for
 * increasing or decreasing the number of rooms and/or changing rooms assigned ratios.
 * The Question: 5_STAR
 * There are 3 rooms in which party is going on lets say room A, B, C.
 * Guests are coming one by one and you have to tell the guest
 * which room to enter. At any point of time each room has to maintain a percentage Lets say the percentage that each
 * room has to maintain is A- 20% , B-30% , C- 50%. You can maintain total count of each room and keep on adding count
 * to respective room as the new guest enters each room.
 * <p>
 * How would you go about it. What formula would you use.
 * Give a generalise formula so that it works if number of rooms increase.
 * <p>
 * Created by haytham.aldokanji on 5/13/16.
 */
public class InputAllocationWhileMaintainingRatios {
    private List<Room> rooms = new ArrayList<>();
    private int totalGuestNumber;

    public static void main(String[] args) {
        InputAllocationWhileMaintainingRatios driver = new InputAllocationWhileMaintainingRatios();
        driver.test1();
        driver.test2();
        driver.test3();
    }

    public void assignNewGuest() {
        totalGuestNumber++;

        Room roomWithSmallestRatioDiff = rooms.get(0);
        double smallestRatioDiff = diffBetweenActualAndAssignedRatios(roomWithSmallestRatioDiff);

        for (int i = 1; i < rooms.size(); i++) {
            Room currentRoom = rooms.get(i);
            double currentRatioDiff = diffBetweenActualAndAssignedRatios(currentRoom);
            if (currentRatioDiff < smallestRatioDiff) {
                roomWithSmallestRatioDiff = currentRoom;
                smallestRatioDiff = currentRatioDiff;
            }
        }
        roomWithSmallestRatioDiff.addGuest();
    }

    private double diffBetweenActualAndAssignedRatios(Room room) {
        double actualRatio = (double) room.numberOfGuests / (double) totalGuestNumber;
        return actualRatio - room.ratio;
    }

    // testing helper
    private void reset() {
        rooms.clear();
        totalGuestNumber = 0;
    }

    private void test1() {
        reset();
        rooms.add(new Room("A", 20));
        rooms.add(new Room("B", 30));
        rooms.add(new Room("C", 50));

        for (int i = 0; i < 40; i++) {
            assignNewGuest();
        }
        rooms.stream().forEach(System.out::println);
        assertThat(rooms.stream()
                .map(Room::getNumberOfGuests)
                .collect(Collectors.toList()), contains(8, 12, 20));
    }

    private void test2() {
        reset();
        rooms.add(new Room("A", 20));
        rooms.add(new Room("B", 30));
        rooms.add(new Room("C", 50));

        for (int i = 0; i < 20; i++) {
            assignNewGuest();
        }
        // add new room and adjust ratio
        rooms.get(2).adjustRatio(10);
        rooms.add(new Room("D", 40));

        for (int i = 0; i < 80; i++) {
            assignNewGuest();
        }

        rooms.stream().forEach(System.out::println);
        assertThat(rooms.stream()
                .map(Room::getNumberOfGuests)
                .collect(Collectors.toList()), contains(20, 30, 10, 40));
    }

    private void test3() {
        reset();
        rooms.add(new Room("A", 20));
        rooms.add(new Room("B", 30));
        rooms.add(new Room("C", 50));

        for (int i = 0; i < 20; i++) {
            assignNewGuest();
        }
        // decommission one room and adjust ratios
        rooms.get(0).adjustRatio(40);
        rooms.get(1).adjustRatio(60);
        // adjust total guest number to reflect decommissioning a room
        totalGuestNumber -= rooms.get(2).getNumberOfGuests();
        rooms.remove(2);

        for (int i = 0; i < 20; i++) {
            assignNewGuest();
        }

        rooms.stream().forEach(System.out::println);
        assertThat(rooms.stream()
                .map(Room::getNumberOfGuests)
                .collect(Collectors.toList()), contains(12, 18));
    }

    private static class Room {
        private final String id;
        private double ratio;
        private int numberOfGuests;

        public Room(String id, double ratio) {
            this.id = id;
            this.ratio = ratio / 100;
        }

        public void adjustRatio(double newRatio) {
            this.ratio = newRatio / 100;
        }

        public void addGuests(int howMany) {
            numberOfGuests += howMany;
        }

        public void addGuest() {
            addGuests(1);
        }

        public int getNumberOfGuests() {
            return numberOfGuests;
        }

        @Override
        public String toString() {
            return "Room{" +
                    "id='" + id + '\'' +
                    ", ratio=" + ratio +
                    ", numberOfGuests=" + numberOfGuests +
                    '}';
        }
    }
}
