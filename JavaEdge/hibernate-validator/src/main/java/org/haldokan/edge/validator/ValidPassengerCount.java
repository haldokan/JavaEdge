package org.haldokan.edge.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PassengerCountValidator.class)
@Documented
public @interface ValidPassengerCount {
    String message() default "Too many passerngers";

    // default has to be empty
    Class<?>[] groups() default {};

    // default has to be empty
    Class<? extends Payload>[] payload() default {};

    VehicleType value() default VehicleType.STANDARD;
}
