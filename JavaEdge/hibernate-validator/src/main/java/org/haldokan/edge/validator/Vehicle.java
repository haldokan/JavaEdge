package org.haldokan.edge.validator;

public interface Vehicle {
	String getManufacturer();

	String getLicensePlate();

	public int getSeatCount();

	boolean isPassedVehicleInspection();

	Driver getDriver();

	int getPassengerCount();
}
