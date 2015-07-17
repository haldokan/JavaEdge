package org.haldokan.edge.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DriverLicenseToVehicleTypeValidator.class)
@Documented
public @interface ValidDriverLicenseForVehicleType {
	String message() default "Driver is not entitled to drive this vehicle type";
	//default has to be empty
	Class<?>[] groups() default {};
	//default has to be empty
	Class<? extends Payload>[] payload() default {};
	VehicleType value() default VehicleType.STANDARD;
}
