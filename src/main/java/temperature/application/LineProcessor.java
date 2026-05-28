package temperature.application;

import temperature.application.ports.LineParser;
import temperature.application.ports.Validator;
import temperature.domain.model.TemperatureReading;
import temperature.domain.validation.TemperatureValidator;
import temperature.infrastructure.input.ParseException;

/**
 * Processes a single input line by parsing and validating it.
 */
public class LineProcessor {

    private final LineParser<TemperatureReading> csvParser;
    private final Validator<TemperatureReading> validator;
    private final BadLineFormatter badLineFormatter;

    public LineProcessor(
            LineParser<TemperatureReading> csvParser,
            Validator<TemperatureReading> validator) {
        this.csvParser = csvParser;
        this.validator = validator;
        this.badLineFormatter = new BadLineFormatter();
    }

    public LineResult processLine(String rawLine, int lineNumber) {
        String line = rawLine.trim();
        if (line.isEmpty()) {
            return LineResult.skipped();
        }

        TemperatureReading reading;
        try {
            reading = csvParser.parse(line);
        } catch (ParseException e) {
            return LineResult.invalid(badLineFormatter.format(lineNumber, line));
        }

        TemperatureValidator.ValidationResult validation = validator.validate(reading);
        if (!validation.isValid()) {
            return LineResult.invalid(badLineFormatter.format(lineNumber, line));
        }

        return LineResult.valid(reading);
    }
}