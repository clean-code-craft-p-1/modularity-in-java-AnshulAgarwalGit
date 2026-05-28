package temperature.domain.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import temperature.domain.model.TemperatureReading;

public class TemperatureValidatorReadingValidationTest {

    private final TemperatureValidator validator = new TemperatureValidator();

    @Test
    public void validateSucceedsForValidReading() {
        TemperatureReading reading = new TemperatureReading("12:30:45", 98.6);
        TemperatureValidator.ValidationResult result = validator.validate(reading);
        assertTrue(result.isValid());
        assertNull(result.getErrorMessage());
    }

    @Test
    public void validateFailsForInvalidTimestamp() {
        TemperatureReading reading = new TemperatureReading("12:30", 98.6);
        TemperatureValidator.ValidationResult result = validator.validate(reading);
        assertFalse(result.isValid());
        assertNotNull(result.getErrorMessage());
    }

    @Test
    public void validateFailsForOutOfRangeTemperature() {
        TemperatureReading reading = new TemperatureReading("12:30:45", 109.0);
        TemperatureValidator.ValidationResult result = validator.validate(reading);
        assertFalse(result.isValid());
        assertNotNull(result.getErrorMessage());
    }
}
