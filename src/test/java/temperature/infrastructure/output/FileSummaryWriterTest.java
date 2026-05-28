package temperature.infrastructure.output;

import org.junit.jupiter.api.Test;
import temperature.domain.model.SummaryReport;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FileSummaryWriter.
 * Validates file output format and content.
 */
public class FileSummaryWriterTest {
    private final FileSummaryWriter writer = new FileSummaryWriter();

    @Test
    public void formatsWithValidData() {
    SummaryReport report = SummaryWriterTestFixtures.twoValidReadingsReport();

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
        SummaryReport report = SummaryWriterTestFixtures.singleValidReadingReport();

        String output = writer.format(report, "myfile.csv");

        assertTrue(output.contains("File analyzed: myfile.csv"));
    }

    @Test
    public void includesBlankLineBeforeInvalidLines() {
        SummaryReport report = SummaryWriterTestFixtures.oneInvalidLineReport();

        String output = writer.format(report, "test.csv");

        // Check that there's a blank line before "Invalid lines:"
        assertTrue(output.contains("\n\nInvalid lines:"));
    }

    @Test
    public void usesShortSeparatorInFile() {
        SummaryReport report = SummaryWriterTestFixtures.singleValidReadingReport();

        String output = writer.format(report, "test.csv");

        assertTrue(output.contains("=================================================="));
    }

    @Test
    public void noBlankLineWhenNoInvalidLines() {
        SummaryReport report = SummaryWriterTestFixtures.singleValidReadingReport();

        String output = writer.format(report, "test.csv");

        assertFalse(output.contains("\n\nInvalid lines:"));
    }
}
