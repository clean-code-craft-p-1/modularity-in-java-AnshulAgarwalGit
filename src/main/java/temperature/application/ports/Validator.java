package temperature.application.ports;

import temperature.domain.validation.TemperatureValidator;

/**
 * Abstraction for validating domain objects.
 */
public interface Validator<T> {
    TemperatureValidator.ValidationResult validate(T item);
}