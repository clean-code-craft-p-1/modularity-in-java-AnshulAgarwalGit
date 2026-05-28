package temperature.infrastructure.input;

import temperature.application.ports.LineParser;
import temperature.domain.model.TemperatureReading;

/**
 * Parses a CSV line into a TemperatureReading.
 * Responsible for CSV tokenization and basic parsing only.
 * Does NOT validate the timestamp format or temperature range.
 */
public class CsvLineParser implements LineParser<TemperatureReading> {

    /**
     * Parses a CSV line into a TemperatureReading.
     * Expects exactly 2 comma-separated fields: timestamp, temperature.
     *
     * @param line the CSV line to parse
     * @return a TemperatureReading with parsed values
     * @throws ParseException if the line doesn't have 2 fields or temperature is not a valid number
     */
    public TemperatureReading parse(String line) throws ParseException {
        String[] parts = line.split(",");
        if (parts.length != 2) {
            throw new ParseException("Expected 2 CSV fields, got " + parts.length);
        }

        String timestamp = parts[0].strip();
        String valueStr = parts[1].strip();

        double temperature;
        try {
            temperature = Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            throw new ParseException("Temperature value is not a valid number: " + valueStr);
        }

        return new TemperatureReading(timestamp, temperature);
    }
}
