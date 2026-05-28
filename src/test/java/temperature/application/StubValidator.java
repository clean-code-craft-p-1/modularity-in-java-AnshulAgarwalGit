package temperature.application;

import temperature.application.ports.Validator;
import temperature.domain.model.TemperatureReading;
import temperature.domain.validation.TemperatureValidator;

class StubValidator implements Validator<TemperatureReading> {

    private final boolean valid;
    private int validateCalls;

    StubValidator() {
        this(true);
    }

    StubValidator(boolean valid) {
        this.valid = valid;
    }

    @Override
    public TemperatureValidator.ValidationResult validate(TemperatureReading item) {
        validateCalls++;
        if (valid) {
            return TemperatureValidator.ValidationResult.valid();
        }
        return TemperatureValidator.ValidationResult.invalid("forced invalid");
    }

    int getValidateCalls() {
        return validateCalls;
    }
}
