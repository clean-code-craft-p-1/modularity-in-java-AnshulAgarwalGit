package temperature;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MainTest {

    @TempDir
    Path tempDir;

    @Test
    void processBatchCreatesExpectedSummaryFile() throws IOException {
        Path inputFile = tempDir.resolve("test_temps.csv");
        List<String> testData = List.of(
                "09:15:30,23.5",
                "09:16:00,24.1",
                "09:16:30,22.8",
                "09:17:00,25.3",
                "09:17:30,23.9",
                "09:18:00,24.7",
                "09:18:30,22.4",
                "09:19:00,26.1",
                "09:19:30,23.2",
                "09:20:00,25.0");

        Files.write(inputFile, testData);

        TemperatureBatchProcessor.processBatch(inputFile.toString());

        Path summaryFile = Path.of(inputFile + "_summary.txt");
        assertTrue(Files.exists(summaryFile), "Expected summary file to be created");

        String content = Files.readString(summaryFile);
        assertAll(
                () -> assertTrue(content.contains("Total readings: 10"), "Expected total readings in summary"),
                () -> assertTrue(content.contains("Valid readings: 10"), "Expected valid readings in summary"),
                () -> assertTrue(content.contains("Errors: 0"), "Expected zero errors in summary"),
                () -> assertTrue(content.contains("Average temperature: 24.10"), "Expected average temperature in summary"));
    }
}