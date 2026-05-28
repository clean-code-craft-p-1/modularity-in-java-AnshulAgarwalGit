package temperature.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TemperatureBatchProcessorBadLinesTest {

    @TempDir
    Path tempDir;

    @Test
    void processBatchIncludesInvalidLinesInSummary() throws IOException {
        Path inputFile = tempDir.resolve("test_temps_with_errors.csv");
        List<String> testData = List.of(
                "09:15:30,23.5",
                "missing-comma-entry",
                "09:16,24.1",
                "09:16:30,not-a-number",
                "09:17:00,250.0",
                "09:18:00,24.7");

        Files.write(inputFile, testData);

        TemperatureBatchProcessor.processBatch(inputFile.toString());

        Path summaryFile = Path.of(inputFile + "_summary.txt");
        assertTrue(Files.exists(summaryFile), "Expected summary file to be created for mixed valid and invalid input");

        String content = Files.readString(summaryFile);
        assertAll(
                () -> assertTrue(content.contains("Total readings: 6"), "Expected total readings in summary"),
                () -> assertTrue(content.contains("Valid readings: 2"), "Expected valid readings in summary"),
                () -> assertTrue(content.contains("Errors: 4"), "Expected error count in summary"),
                () -> assertTrue(content.contains("Invalid lines:"), "Expected invalid lines section in summary"),
                () -> assertTrue(content.contains("  Line 2: missing-comma-entry"), "Expected malformed CSV line to be reported"),
                () -> assertTrue(content.contains("  Line 3: 09:16,24.1"), "Expected invalid timestamp line to be reported"),
                () -> assertTrue(content.contains("  Line 4: 09:16:30,not-a-number"), "Expected non-numeric temperature line to be reported"),
                () -> assertTrue(content.contains("  Line 5: 09:17:00,250.0"), "Expected out-of-range temperature line to be reported"));
    }
}