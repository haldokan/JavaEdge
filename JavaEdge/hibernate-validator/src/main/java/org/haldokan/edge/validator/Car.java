package org.haldokan.edge.validator;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.haldokan.edge.validator.ValidationGroups.OverallChecks;

//@ValidPassengerCount(value = VehicleType.STANDARD, message = "Number of dudes in the car should not exceed the seat count", groups = ValidationGroups.CapacityChecks.class, payload = ViolationSeverity.Warn.class)
//@ValidDriverLicenseForVehicleType(value = VehicleType.STANDARD, message = "Your license isn't good for this vehicle type dude!", payload = ViolationSeverity.Error.class)
// This annotation compses the 2 above that therefor can replace them
@VehicleChecks(payload = ViolationSeverity.Error.class, groups = OverallChecks.class)
public class Car implements Vehicle {
    @NotNull
    private String manufacturer;

    @NotNull
    @Size(min = 2, max = 14)
    private String licensePlate;

    @Min(2)
    private int seatCount;

    @AssertTrue(message = "The car has to pass the vehicle inspection first", groups = ValidationGroups.CarChecks.class)
    private boolean passedVehicleInspection;

    @Valid
    private Driver driver;

    private int passengerCount;

    public Car(String manufacturer, String licencePlate, int seatCount) {
	this.manufacturer = manufacturer;
	this.licensePlate = licencePlate;
	this.seatCount = seatCount;
    }

    public String getManufacturer() {
	return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
	this.manufacturer = manufacturer;
    }

    public String getLicensePlate() {
	return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
	this.licensePlate = licensePlate;
    }

    public int getSeatCount() {
	return seatCount;
    }

    public void setSeatCount(int seatCount) {
	this.seatCount = seatCount;
    }

    public boolean isPassedVehicleInspection() {
	return passedVehicleInspection;
    }

    public void setPassedVehicleInspection(boolean passedVehicleInspection) {
	this.passedVehicleInspection = passedVehicleInspection;
    }

    public Driver getDriver() {
	return driver;
    }

    public void setDriver(Driver driver) {
	this.driver = driver;
    }

    /**
     * @return the passengerCount
     */
    public int getPassengerCount() {
	return passengerCount;
    }

    /**
     * @param passengerCount
     *            the passengerCount to set
     */
    public void setPassengerCount(int passengerCount) {
	this.passengerCount = passengerCount;
    }

    @Override
    public String toString() {
	return "Car [manufacturer=" + manufacturer + ", licensePlate=" + licensePlate + ", seatCount=" + seatCount
		+ ", passedVehicleInspection=" + passedVehicleInspection + ", driver=" + driver + ", passengerCount="
		+ passengerCount + "]";
    }
}