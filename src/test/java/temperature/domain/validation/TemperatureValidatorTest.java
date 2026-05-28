package temperature.domain.validation;

import org.junit.jupiter.api.Test;
import temperature.domain.model.TemperatureReading;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TemperatureValidator.
 * Validates individual validation rules and combined validation.
 */
public class TemperatureValidatorTest {

    private final TemperatureValidator validator = new TemperatureValidator();

    @Test
    public void validTimestampFormat() {
        assertTrue(TemperatureValidator.isValidTimestampFormat("12:30:45"));
        assertTrue(TemperatureValidator.isValidTimestampFormat("00:00:00"));
        assertTrue(TemperatureValidator.isValidTimestampFormat("23:59:59"));
    }

    @Test
    public void invalidTimestampFormat() {
        assertFalse(TemperatureValidator.isValidTimestampFormat("12:30"));
        assertFalse(TemperatureValidator.isValidTimestampFormat("12:30:45:00"));
        assertFalse(TemperatureValidator.isValidTimestampFormat("12-30-45"));
    }

    @Test
    public void validTemperatureRange() {
        assertTrue(TemperatureValidator.isValidTemperatureRange(20.0));
        assertTrue(TemperatureValidator.isValidTemperatureRange(-100.0));
        assertTrue(TemperatureValidator.isValidTemperatureRange(200.0));
        assertTrue(TemperatureValidator.isValidTemperatureRange(0.0));
    }

    @Test
    public void invalidTemperatureTooLow() {
        assertFalse(TemperatureValidator.isValidTemperatureRange(-100.1));
        assertFalse(TemperatureValidator.isValidTemperatureRange(-200.0));
    }

    @Test
    public void invalidTemperatureTooHigh() {
        assertFalse(TemperatureValidator.isValidTemperatureRange(200.1));
        assertFalse(TemperatureValidator.isValidTemperatureRange(300.0));
    }

    @Test
    public void validateSucceedsForValidReading() {
        TemperatureReading reading = new TemperatureReading("12:30:45", 25.0);
        TemperatureValidator.ValidationResult result = validator.validate(reading);
        assertTrue(result.isValid());
        assertNull(result.getErrorMessage());
    }

    @Test
    public void validateFailsForInvalidTimestamp() {
        TemperatureReading reading = new TemperatureReading("12:30", 25.0);
        TemperatureValidator.ValidationResult result = validator.validate(reading);
        assertFalse(result.isValid());
        assertNotNull(result.getErrorMessage());
    }

    @Test
    public void validateFailsForOutOfRangeTemperature() {
        TemperatureReading reading = new TemperatureReading("12:30:45", 250.0);
        TemperatureValidator.ValidationResult result = validator.validate(reading);
        assertFalse(result.isValid());
        assertNotNull(result.getErrorMessage());
    }
}
