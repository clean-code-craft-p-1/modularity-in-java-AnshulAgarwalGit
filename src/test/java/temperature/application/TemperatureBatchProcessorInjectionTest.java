package temperature.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import temperature.infrastructure.input.ParseException;

class TemperatureBatchProcessorInjectionTest {

    @TempDir
    Path tempDir;

    @Test
    void processUsesInjectedCollaboratorsAndWritesExpectedSummary() throws IOException, ParseException {
        Path inputFile = tempDir.resolve("injected.csv");
        Path summaryFile = Path.of(inputFile + "_summary.txt");

        StubReader reader = new StubReader(List.of("12:00:00,22.0", "", "12:30:00,24.0"));
        StubParser parser = new StubParser();
        StubValidator validator = new StubValidator();
        StubWriter consoleWriter = new StubWriter("console-output\n");
        StubWriter fileWriter = new StubWriter("file-output\n");

        TemperatureBatchProcessor processor = new TemperatureBatchProcessor(
                reader,
                parser,
                validator,
                consoleWriter,
                fileWriter);

        processor.process(inputFile.toString());

        assertTrue(Files.exists(summaryFile), "Expected summary file to be written");
        assertEquals("file-output\n", Files.readString(summaryFile));
        assertEquals(2, parser.getParseCalls());
        assertEquals(2, validator.getValidateCalls());
        assertEquals(1, consoleWriter.getFormatCalls());
        assertEquals(1, fileWriter.getFormatCalls());
    }
}
