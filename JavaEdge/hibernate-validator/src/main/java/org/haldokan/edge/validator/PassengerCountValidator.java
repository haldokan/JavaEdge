package org.haldokan.edge.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PassengerCountValidator implements
		ConstraintValidator<ValidPassengerCount, Vehicle> {
	private VehicleType carType;

	// note the params is the annotation
	@Override
	public void initialize(ValidPassengerCount validPassengerCount) {
		this.carType = validPassengerCount.value();
	}

	@Override
	public boolean isValid(Vehicle vehicle, ConstraintValidatorContext context) {
		System.out.println(this.hashCode());
		if (VehicleType.STANDARD == carType || VehicleType.SUV == carType)
			return vehicle.getPassengerCount() <= vehicle.getSeatCount();
		else if (VehicleType.TRUCK == carType)
			return true;
		else
			throw new IllegalArgumentException("Invalid car type" + carType);
	}

}
