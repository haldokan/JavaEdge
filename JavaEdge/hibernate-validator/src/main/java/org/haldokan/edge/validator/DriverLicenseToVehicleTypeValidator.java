package org.haldokan.edge.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DriverLicenseToVehicleTypeValidator implements
        ConstraintValidator<ValidDriverLicenseForVehicleType, Vehicle> {
    private VehicleType type;

    @Override
    public void initialize(ValidDriverLicenseForVehicleType constraintAnnotation) {
        this.type = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Vehicle vehicle, ConstraintValidatorContext context) {
        return type == vehicle.getDriver().getLicensedFor();
    }

}
