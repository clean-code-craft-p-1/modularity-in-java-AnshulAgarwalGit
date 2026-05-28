package temperature;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FileSummaryWriter.
 * Validates file output format and content.
 */
public class FileSummaryWriterTest {
    private final FileSummaryWriter writer = new FileSummaryWriter();

    @Test
    public void formatsWithValidData() {
        List<TemperatureReading> readings = Arrays.asList(
                new TemperatureReading("12:00:00", 20.0),
                new TemperatureReading("12:30:00", 30.0)
        );
        TemperatureStatistics stats = new TemperatureStatistics(readings);
        SummaryReport report = new SummaryReport(2, 2, 0, stats, Collections.emptyList());

        String output = writer.format(report, "test.csv");

        assertTrue(output.contains("=================================================="));
        assertTrue(output.contains("Temperature Analysis Summary"));
        assertTrue(output.contains("File analyzed: test.csv"));
        assertTrue(output.contains("Total readings: 2"));
        assertTrue(output.contains("Valid readings: 2"));
        assertTrue(output.contains("Errors: 0"));
        assertTrue(output.contains("Max temperature: 30.00"));
        assertTrue(output.contains("Min temperature: 20.00"));
        assertTrue(output.contains("Average temperature: 25.00"));
    }

    @Test
    public void includesFileAnalyzedLine() {
        List<TemperatureReading> readings = Arrays.asList(
                new TemperatureReading("12:00:00", 20.0)
        );
        TemperatureStatistics stats = new TemperatureStatistics(readings);
        SummaryReport report = new SummaryReport(1, 1, 0, stats, Collections.emptyList());

        String output = writer.format(report, "myfile.csv");

        assertTrue(output.contains("File analyzed: myfile.csv"));
    }

    @Test
    public void includesBlankLineBeforeInvalidLines() {
        List<TemperatureReading> readings = Arrays.asList(
                new TemperatureReading("12:00:00", 20.0)
        );
        TemperatureStatistics stats = new TemperatureStatistics(readings);
        List<String> badLines = Arrays.asList("  Line 2: invalid,data");
        SummaryReport report = new SummaryReport(2, 1, 1, stats, badLines);

        String output = writer.format(report, "test.csv");

        // Check that there's a blank line before "Invalid lines:"
        assertTrue(output.contains("\n\nInvalid lines:"));
    }

    @Test
    public void usesShortSeparatorInFile() {
        List<TemperatureReading> readings = Arrays.asList(
                new TemperatureReading("12:00:00", 20.0)
        );
        TemperatureStatistics stats = new TemperatureStatistics(readings);
        SummaryReport report = new SummaryReport(1, 1, 0, stats, Collections.emptyList());

        String output = writer.format(report, "test.csv");

        assertTrue(output.contains("=================================================="));
    }

    @Test
    public void noBlankLineWhenNoInvalidLines() {
        List<TemperatureReading> readings = Arrays.asList(
                new TemperatureReading("12:00:00", 20.0)
        );
        TemperatureStatistics stats = new TemperatureStatistics(readings);
        SummaryReport report = new SummaryReport(1, 1, 0, stats, Collections.emptyList());

        String output = writer.format(report, "test.csv");

        assertFalse(output.contains("\n\nInvalid lines:"));
    }
}
