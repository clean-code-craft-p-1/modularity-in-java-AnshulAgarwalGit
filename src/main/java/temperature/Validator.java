package temperature;

/**
 * Abstraction for validating domain objects.
 */
public interface Validator<T> {
    TemperatureValidator.ValidationResult validate(T item);
}