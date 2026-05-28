package temperature.infrastructure.input;

import org.junit.jupiter.api.Test;
import temperature.domain.model.TemperatureReading;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CsvLineParser.
 * Validates CSV tokenization and basic parsing without full validation.
 */
public class CsvLineParserTest {
    private final CsvLineParser parser = new CsvLineParser();

    @Test
    public void parsesValidCsvLine() throws ParseException {
        TemperatureReading reading = parser.parse("12:30:45,23.5");
        assertEquals("12:30:45", reading.getTimestamp());
        assertEquals(23.5, reading.getFahrenheit());
    }

    @Test
    public void parsesWithWhitespace() throws ParseException {
        TemperatureReading reading = parser.parse("  12:30:45  ,  23.5  ");
        assertEquals("12:30:45", reading.getTimestamp());
        assertEquals(23.5, reading.getFahrenheit());
    }

    @Test
    public void throwsExceptionForMissingTemperature() {
        assertThrows(ParseException.class, () -> parser.parse("12:30:45"));
    }

    @Test
    public void throwsExceptionForTooManyFields() {
        assertThrows(ParseException.class, () -> parser.parse("12:30:45,23.5,extra"));
    }

    @Test
    public void throwsExceptionForNonNumericTemperature() {
        assertThrows(ParseException.class, () -> parser.parse("12:30:45,invalid"));
    }

    @Test
    public void parsesNegativeTemperature() throws ParseException {
        TemperatureReading reading = parser.parse("12:30:45,-15.3");
        assertEquals(-15.3, reading.getFahrenheit());
    }

    @Test
    public void parsesLargeTemperature() throws ParseException {
        TemperatureReading reading = parser.parse("12:30:45,250.0");
        assertEquals(250.0, reading.getFahrenheit());
    }
}
