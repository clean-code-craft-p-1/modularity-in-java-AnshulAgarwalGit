package temperature.application;

import java.util.List;
import temperature.domain.model.TemperatureReading;

/**
 * Immutable aggregate of parsed batch results.
 */
class ProcessingData {

    private final List<TemperatureReading> validReadings;
    private final List<String> badLines;

    ProcessingData(List<TemperatureReading> validReadings, List<String> badLines) {
        this.validReadings = validReadings;
        this.badLines = badLines;
    }

    List<TemperatureReading> getValidReadings() {
        return validReadings;
    }

    List<String> getBadLines() {
        return badLines;
    }
}
