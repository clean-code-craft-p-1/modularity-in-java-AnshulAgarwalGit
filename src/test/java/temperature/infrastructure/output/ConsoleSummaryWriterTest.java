package temperature.infrastructure.output;

import org.junit.jupiter.api.Test;
import temperature.domain.model.SummaryReport;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConsoleSummaryWriter.
 * Validates console output format and content.
 */
public class ConsoleSummaryWriterTest {
    private final ConsoleSummaryWriter writer = new ConsoleSummaryWriter();

    @Test
    public void formatsWithValidData() {
    SummaryReport report = SummaryWriterTestFixtures.twoValidReadingsReport();

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
        SummaryReport report = SummaryWriterTestFixtures.oneInvalidLineReport();

        String output = writer.format(report, "test.csv");

        assertTrue(output.contains("Invalid lines:"));
        assertTrue(output.contains("  Line 2: invalid,data"));
    }

    @Test
    public void noFileAnalyzedLineInConsole() {
        SummaryReport report = SummaryWriterTestFixtures.singleValidReadingReport();

        String output = writer.format(report, "test.csv");

        assertFalse(output.contains("File analyzed:"));
    }

    @Test
    public void usesLongerSeparatorInConsole() {
        SummaryReport report = SummaryWriterTestFixtures.singleValidReadingReport();

        String output = writer.format(report, "test.csv");

        assertTrue(output.contains("============================================================"));
        // Verify the 60-char separator is used for console output
    }
}
