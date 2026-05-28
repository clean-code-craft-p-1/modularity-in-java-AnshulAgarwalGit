package temperature;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConsoleSummaryWriter.
 * Validates console output format and content.
 */
public class ConsoleSummaryWriterTest {
    private final ConsoleSummaryWriter writer = new ConsoleSummaryWriter();

    @Test
    public void formatsWithValidData() {
        List<TemperatureReading> readings = Arrays.asList(
                new TemperatureReading("12:00:00", 20.0),
                new TemperatureReading("12:30:00", 30.0)
        );
        TemperatureStatistics stats = new TemperatureStatistics(readings);
        SummaryReport report = new SummaryReport(2, 2, 0, stats, Collections.emptyList());

        String output = writer.format(report, "test.csv");

        assertTrue(output.contains("============================================================"));
        assertTrue(output.contains("Temperature Analysis Summary"));
        assertTrue(output.contains("Total readings: 2"));
        assertTrue(output.contains("Valid readings: 2"));
        assertTrue(output.contains("Errors: 0"));
        assertTrue(output.contains("Max temperature: 30.00"));
        assertTrue(output.contains("Min temperature: 20.00"));
        assertTrue(output.contains("Average temperature: 25.00"));
    }

    @Test
    public void includesInvalidLines() {
        List<TemperatureReading> readings = Arrays.asList(
                new TemperatureReading("12:00:00", 20.0)
        );
        TemperatureStatistics stats = new TemperatureStatistics(readings);
        List<String> badLines = Arrays.asList("  Line 2: invalid,data");
        SummaryReport report = new SummaryReport(2, 1, 1, stats, badLines);

        String output = writer.format(report, "test.csv");

        assertTrue(output.contains("Invalid lines:"));
        assertTrue(output.contains("  Line 2: invalid,data"));
    }

    @Test
    public void noFileAnalyzedLineInConsole() {
        List<TemperatureReading> readings = Arrays.asList(
                new TemperatureReading("12:00:00", 20.0)
        );
        TemperatureStatistics stats = new TemperatureStatistics(readings);
        SummaryReport report = new SummaryReport(1, 1, 0, stats, Collections.emptyList());

        String output = writer.format(report, "test.csv");

        assertFalse(output.contains("File analyzed:"));
    }

    @Test
    public void usesLongerSeparatorInConsole() {
        List<TemperatureReading> readings = Arrays.asList(
                new TemperatureReading("12:00:00", 20.0)
        );
        TemperatureStatistics stats = new TemperatureStatistics(readings);
        SummaryReport report = new SummaryReport(1, 1, 0, stats, Collections.emptyList());

        String output = writer.format(report, "test.csv");

        assertTrue(output.contains("============================================================"));
        // Verify the 60-char separator is used for console output
    }
}
