package temperature.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TemperatureBatchProcessorNoValidReadingsTest {

    @TempDir
    Path tempDir;

    @Test
    void processSkipsOutputsWhenNoValidReadings() throws IOException {
        Path inputFile = tempDir.resolve("no_valid.csv");
        Path summaryFile = Path.of(inputFile + "_summary.txt");

        StubReader reader = new StubReader(List.of("12:00:00,22.0"));
        StubParser parser = new StubParser();
        StubValidator validator = new StubValidator(false);
        StubWriter consoleWriter = new StubWriter("unused");
        StubWriter fileWriter = new StubWriter("unused");

        TemperatureBatchProcessor processor = new TemperatureBatchProcessor(
                reader,
                parser,
                validator,
                consoleWriter,
                fileWriter);

        processor.process(inputFile.toString());

        assertFalse(Files.exists(summaryFile), "Expected no summary file when no valid readings exist");
        assertEquals(0, consoleWriter.getFormatCalls());
        assertEquals(0, fileWriter.getFormatCalls());
    }
}
