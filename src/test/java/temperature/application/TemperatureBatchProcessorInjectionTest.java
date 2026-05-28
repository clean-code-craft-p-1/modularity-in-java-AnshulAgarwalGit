package temperature.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import temperature.application.ports.LineParser;
import temperature.application.ports.ReportWriter;
import temperature.application.ports.TemperatureLineReader;
import temperature.application.ports.Validator;
import temperature.domain.model.SummaryReport;
import temperature.domain.model.TemperatureReading;
import temperature.domain.validation.TemperatureValidator;
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
        assertEquals(2, parser.parseCalls);
        assertEquals(2, validator.validateCalls);
        assertEquals(1, consoleWriter.formatCalls);
        assertEquals(1, fileWriter.formatCalls);
    }

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
        assertEquals(0, consoleWriter.formatCalls);
        assertEquals(0, fileWriter.formatCalls);
    }

    private static class StubReader implements TemperatureLineReader {
        private final List<String> lines;

        private StubReader(List<String> lines) {
            this.lines = lines;
        }

        @Override
        public List<String> readLines(String source) {
            return lines;
        }
    }

    private static class StubParser implements LineParser<TemperatureReading> {
        private int parseCalls;

        @Override
        public TemperatureReading parse(String line) throws ParseException {
            parseCalls++;
            String[] parts = line.split(",");
            if (parts.length != 2) {
                throw new ParseException("Malformed line");
            }
            return new TemperatureReading(parts[0], Double.parseDouble(parts[1]));
        }
    }

    private static class StubValidator implements Validator<TemperatureReading> {
        private final boolean valid;
        private int validateCalls;

        private StubValidator() {
            this(true);
        }

        private StubValidator(boolean valid) {
            this.valid = valid;
        }

        @Override
        public TemperatureValidator.ValidationResult validate(TemperatureReading item) {
            validateCalls++;
            if (valid) {
                return TemperatureValidator.ValidationResult.valid();
            }
            return TemperatureValidator.ValidationResult.invalid("forced invalid");
        }
    }

    private static class StubWriter implements ReportWriter {
        private final String response;
        private int formatCalls;

        private StubWriter(String response) {
            this.response = response;
        }

        @Override
        public String format(SummaryReport report, String filename) {
            formatCalls++;
            return response;
        }
    }
}
