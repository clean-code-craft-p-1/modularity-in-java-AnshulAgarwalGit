package temperature.app;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import temperature.application.TemperatureBatchProcessor;

class MainTest {

    @TempDir
    Path tempDir;

    @Test
    void processBatchCreatesExpectedSummaryFile() throws IOException {
        Path inputFile = tempDir.resolve("test_temps.csv");
        List<String> testData = List.of(
            "09:15:30,98.6",
            "09:16:00,98.4",
            "09:16:30,98.7",
            "09:17:00,99.1",
            "09:17:30,98.9",
            "09:18:00,98.3",
            "09:18:30,98.5",
            "09:19:00,99.0",
            "09:19:30,98.8",
            "09:20:00,97.9");

        Files.write(inputFile, testData);

        TemperatureBatchProcessor.processBatch(inputFile.toString());

        Path summaryFile = Path.of(inputFile + "_summary.txt");
        assertTrue(Files.exists(summaryFile), "Expected summary file to be created");

        String content = Files.readString(summaryFile);
        assertAll(
                () -> assertTrue(content.contains("Total readings: 10"), "Expected total readings in summary"),
                () -> assertTrue(content.contains("Valid readings: 10"), "Expected valid readings in summary"),
                () -> assertTrue(content.contains("Errors: 0"), "Expected zero errors in summary"),
            () -> assertTrue(content.contains("Average temperature: 98.62"), "Expected average temperature in summary"));
    }
}