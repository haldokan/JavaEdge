package org.haldokan.edge.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

/**
 * Using a custom ConstraintValidatorFactory offers for example the possibility to use dependency injection in
 * constraint validator implementations.
 *
 * @author haldokan
 */
public class CustomConstraintValidatorFactory implements ConstraintValidatorFactory {

    // How is the initialize called?
    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        if (key.isAssignableFrom(PassengerCountValidator.class)) {
            PassengerCountValidator pcv = new PassengerCountValidator();
            pcv.initialize(Car.class.getAnnotation(ValidPassengerCount.class));
            return (T) pcv;
        }
        return null;
    }

    @Override
    public void releaseInstance(ConstraintValidator<?, ?> cv) {
        cv = null;
    }

}
