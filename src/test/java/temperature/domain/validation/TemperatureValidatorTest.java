package temperature.domain.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for TemperatureValidator.
 * Validates individual validation rules and combined validation.
 */
public class TemperatureValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"12:30:45", "00:00:00", "23:59:59"})
    public void validTimestampFormat(String timestamp) {
        assertTrue(TemperatureValidator.isValidTimestampFormat(timestamp));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12:30", "12:30:45:00", "12-30-45"})
    public void invalidTimestampFormat(String timestamp) {
        assertFalse(TemperatureValidator.isValidTimestampFormat(timestamp));
    }

    @ParameterizedTest
    @ValueSource(doubles = {98.6, 95.0, 107.0, 100.4})
    public void validTemperatureRange(double fahrenheit) {
        assertTrue(TemperatureValidator.isValidTemperatureRange(fahrenheit));
    }

    @ParameterizedTest
    @ValueSource(doubles = {94.9, 90.0, 107.1, 110.0})
    public void invalidTemperatureRange(double fahrenheit) {
        assertFalse(TemperatureValidator.isValidTemperatureRange(fahrenheit));
    }
}
