package org.haldokan.edge.interviewquest.amazon;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

/**
 * My solution to an Amazon interview question - Designed and implemented the scheduling logic
 * If all elevators are going in the same direction and all passed the requester location, requester
 * will have to wait until the first elevator becomes stationary. The retry logic and the elevator resetting its
 * direction to STATIONARY when no stops are requested from it, insures that happening.
 * <p>
 * The Question: 4_STAR
 * <p>
 * Design a system that most efficiently selects an elevator in a building based on where the
 * elevator is located and headed and on where the user is located and headed.
 * <p>
 * Created by haytham.aldokanji on 7/29/16.
 */
public class ElevatorSystemOptimization {
    private final int numberOfFloors;
    private final Elevator[] elevators;

    public ElevatorSystemOptimization(int numberOfFloors, Elevator[] elevators) {
        this.numberOfFloors = numberOfFloors;
        this.elevators = Arrays.copyOf(elevators, elevators.length);
    }

    public Elevator requestElevator(int floor, Direction requesterDirection) throws Exception {
        Optional<Elevator> potentialElevator;
        do {
            potentialElevator = doRequestElevator(floor, requesterDirection);
            if (!potentialElevator.isPresent()) {
                Thread.sleep(5000);
            }
        } while (!potentialElevator.isPresent());

        Elevator elevator = potentialElevator.get();
        elevator.setDirection(requesterDirection);
        elevator.setStop(floor);

        return elevator;
    }

    public Optional<Elevator> doRequestElevator(int floor, Direction requesterDirection) {
        // closest stationary stationary
        Optional<Elevator> closestStationaryElevator = Arrays.stream(elevators)
                .filter(elevator -> elevator.getDirection() == Direction.STATIONARY)
                .min((e1, e2) -> (Math.abs(e1.getLocation() - floor) - Math.abs(e2.getLocation() - floor)));

        // closest elevator moving in the same direction but has not passed the requester
        Optional<Elevator> closestMovingElevator = Arrays.stream(elevators)
                .filter(elevator -> elevator.direction == requesterDirection)
                .filter(elevator1 -> requesterDirection == Direction.UP ? elevator1.getLocation() <= floor
                        : elevator1.getLocation() >= floor)
                .min((e1, e2) -> Math.abs(e1.getLocation() - floor) - Math.abs(e2.getLocation() - floor));

        if (closestStationaryElevator.isPresent() && closestMovingElevator.isPresent()) {
            return Math.abs(closestStationaryElevator.get().getLocation() - floor) <
                    Math.abs(closestMovingElevator.get().getLocation() - floor)
                    ? closestStationaryElevator
                    : closestMovingElevator;
        }
        return closestStationaryElevator.isPresent() ? closestStationaryElevator : closestMovingElevator;
    }

    private enum Direction {UP, DOWN, STATIONARY}

    private static final class Elevator {
        private final String id;
        private Direction direction;
        private boolean[] stopAtFloors;

        public Elevator(String id, int numberOfFloors) {
            this.id = id;
            direction = Direction.STATIONARY;
            stopAtFloors = new boolean[numberOfFloors];
        }

        public void setStop(int floor) {
            stopAtFloors[floor] = true;
        }

        // reasonable to assume that the elevator should know it's location
        public int getLocation() {
            return new Random().nextInt(stopAtFloors.length);
        }

        public String getId() {
            return id;
        }

        public Direction getDirection() {
            return direction;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        // elevator should clear the floor stop when it reaches the floor and stops at it
        private void clearStop(int floor) {
            stopAtFloors[floor] = false;
            if (stopAtFloors.length == 0) {
                direction = Direction.STATIONARY; // so elevator does not go thru the roof!
            }
        }
    }
}
