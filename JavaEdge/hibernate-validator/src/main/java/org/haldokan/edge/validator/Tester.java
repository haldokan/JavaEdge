package org.haldokan.edge.validator;

import org.haldokan.edge.validator.ValidationGroups.OverallChecks;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.*;
import javax.validation.groups.Default;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class Tester {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeClass
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testCarPassengerCount() {
        // create a car and check that everything is ok with it.
        Car car = new Car("Morris", "DD-AB-123", 2);
        Driver john = new Driver("John Doe");
        john.setAge(18);
        john.setLicensedFor(VehicleType.TRUCK);
        car.setDriver(john);

        Set<ConstraintViolation<Vehicle>> constraintViolations = validator.validate(car, OverallChecks.class);
        assertEquals(1, constraintViolations.size());

        car.setPassengerCount(3);
        constraintViolations = validator.validate(car, OverallChecks.class);
        assertEquals(2, constraintViolations.size());

        for (ConstraintViolation<Vehicle> cv : constraintViolations) {
            System.out.println("msg = " + cv.getMessage() + ", invalidValue = " + cv.getInvalidValue());
            Set<Class<? extends Payload>> payloads = cv.getConstraintDescriptor().getPayload();
            for (Class<? extends Payload> pl : payloads) {
                System.out.println(pl);
                if (pl == ViolationSeverity.Warn.class) {
                    System.out.println("onWarn");
                }
                if (pl == ViolationSeverity.Error.class) {
                    System.out.println("onError");
                }
            }
        }
    }

    @Test
    public void testTruckPassengerCount() {
        // create a car and check that everything is ok with it.
        Vehicle truck = new Truck("Morris", "DD-AB-123", 2);
        Set<ConstraintViolation<Vehicle>> constraintViolations = validator.validate(truck);
        assertEquals(0, constraintViolations.size());

        ((Truck) truck).setPassengerCount(3);
        constraintViolations = validator.validate(truck, ValidationGroups.CapacityChecks.class);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void testTbd() {
        // create a car and check that everything is ok with it.
        Car car = new Car("Morris", "DD-AB-123", 2);
        Driver joe = new Driver("John");
        joe.setAge(30);
        joe.passedDrivingTest(true);
        joe.setLicensedFor(VehicleType.STANDARD);

        car.setDriver(joe);
        Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);
        assertEquals(0, constraintViolations.size());

        // but has it passed the vehicle inspection?
        constraintViolations = validator.validate(car, ValidationGroups.CarChecks.class);

        assertEquals(1, constraintViolations.size());
        assertEquals("The car has to pass the vehicle inspection first", constraintViolations.iterator().next()
                .getMessage());

        // let's go to the vehicle inspection
        car.setPassedVehicleInspection(true);
        assertEquals(0, validator.validate(car).size());

        // now let's add a driver. He is 18, but has not passed the driving test
        // yet
        Driver john = new Driver("John Doe");
        john.setAge(18);
        car.setDriver(john);
        constraintViolations = validator.validate(car, ValidationGroups.DriverChecks.class);
        assertEquals(1, constraintViolations.size());
        assertEquals("You first have to pass the driving test", constraintViolations.iterator().next().getMessage());

        // ok, John passes the test
        john.passedDrivingTest(true);
        assertEquals(0, validator.validate(car, ValidationGroups.DriverChecks.class).size());

        // just checking that everything is in order now
        assertEquals(
                0,
                validator.validate(car, Default.class, ValidationGroups.CarChecks.class,
                        ValidationGroups.DriverChecks.class).size());
        System.out.println("Finished validation!");
    }

}
