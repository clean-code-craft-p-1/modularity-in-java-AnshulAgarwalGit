package temperature;

/**
 * Validates parsed TemperatureReading values against business rules.
 * Encapsulates validation logic for timestamp format and temperature range.
 */
public class TemperatureValidator implements Validator<TemperatureReading> {

    private static final int MIN_TEMP = -100;
    private static final int MAX_TEMP = 200;

    /**
     * Validates that the timestamp has exactly 3 colon-separated parts.
     *
     * @param timestamp the timestamp string to validate
     * @return true if timestamp format is valid
     */
    public static boolean isValidTimestampFormat(String timestamp) {
        String[] parts = timestamp.split(":");
        return parts.length == 3;
    }

    /**
     * Validates that the temperature value is within acceptable range.
     *
     * @param celsius the temperature value to validate
     * @return true if temperature is within [-100, 200]
     */
    public static boolean isValidTemperatureRange(double celsius) {
        return celsius >= MIN_TEMP && celsius <= MAX_TEMP;
    }

    /**
     * Validates a TemperatureReading against all business rules.
     * Returns a ValidationResult that indicates success or provides an error reason.
     *
     * @param reading the reading to validate
     * @return ValidationResult with isValid flag and optional error message
     */
    @Override
    public ValidationResult validate(TemperatureReading reading) {
        if (!isValidTimestampFormat(reading.getTimestamp())) {
            return ValidationResult.invalid("Invalid timestamp format");
        }

        if (!isValidTemperatureRange(reading.getCelsius())) {
            return ValidationResult.invalid("Temperature out of range");
        }

        return ValidationResult.valid();
    }

    /**
     * Backward-compatible static entrypoint for existing callers.
     */
    public static ValidationResult validateReading(TemperatureReading reading) {
        return new TemperatureValidator().validate(reading);
    }

    /**
     * Simple result type for validation outcomes.
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;

        private ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult invalid(String errorMessage) {
            return new ValidationResult(false, errorMessage);
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
