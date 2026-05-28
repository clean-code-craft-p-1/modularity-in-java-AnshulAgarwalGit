package temperature.application;

import temperature.domain.model.TemperatureReading;

/**
 * Result of processing one input line.
 */
public class LineResult {

    private final TemperatureReading reading;
    private final String badLine;

    private LineResult(TemperatureReading reading, String badLine) {
        this.reading = reading;
        this.badLine = badLine;
    }

    public static LineResult valid(TemperatureReading reading) {
        return new LineResult(reading, null);
    }

    public static LineResult invalid(String badLine) {
        return new LineResult(null, badLine);
    }

    public static LineResult skipped() {
        return new LineResult(null, null);
    }

    public TemperatureReading getReading() {
        return reading;
    }

    public String getBadLine() {
        return badLine;
    }
}
