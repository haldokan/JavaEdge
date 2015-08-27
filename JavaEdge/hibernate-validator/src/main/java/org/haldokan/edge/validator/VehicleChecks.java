package org.haldokan.edge.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

//we cannot access the payloads when composing their constraint. The pay load of the composing annotation is the one retrieved
@ValidPassengerCount(value = VehicleType.STANDARD, message = "Number of dudes in the car should not exceed the seat count", groups = ValidationGroups.CapacityChecks.class, payload = ViolationSeverity.Warn.class)
@ValidDriverLicenseForVehicleType(value = VehicleType.STANDARD, message = "Your license isn't good for this vehicle type dude!", payload = ViolationSeverity.Error.class)
@Constraint(validatedBy = {})
// @ReportAsSingleViolation
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VehicleChecks {
    String message() default "Vehicle checks failed";

    // default has to be empty
    Class<?>[] groups() default {};

    // default has to be empty
    Class<? extends Payload>[] payload() default {};
}
