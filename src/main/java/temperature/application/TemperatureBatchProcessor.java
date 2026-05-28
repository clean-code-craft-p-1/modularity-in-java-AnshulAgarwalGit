package temperature.application;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import temperature.application.ports.LineParser;
import temperature.application.ports.ReportWriter;
import temperature.application.ports.TemperatureLineReader;
import temperature.application.ports.Validator;
import temperature.domain.model.SummaryReport;
import temperature.domain.model.TemperatureReading;
import temperature.domain.model.TemperatureStatistics;
import temperature.domain.validation.TemperatureValidator;
import temperature.infrastructure.input.CsvLineParser;
import temperature.infrastructure.input.ParseException;
import temperature.infrastructure.input.TemperatureFileReader;
import temperature.infrastructure.output.ConsoleSummaryWriter;
import temperature.infrastructure.output.FileSummaryWriter;

/**
 * Orchestrates temperature data batch processing.
 * Delegates parsing, validation, statistics, and reporting to focused collaborators.
 * Remains the stable public API while internally modular.
 */
public class TemperatureBatchProcessor {

    private static final TemperatureBatchProcessor DEFAULT_PROCESSOR = new TemperatureBatchProcessor();

    private final TemperatureLineReader fileReader;
    private final LineParser<TemperatureReading> csvParser;
    private final Validator<TemperatureReading> validator;
    private final ReportWriter consoleWriter;
    private final ReportWriter fileWriter;

    public TemperatureBatchProcessor() {
        this(
                new TemperatureFileReader(),
                new CsvLineParser(),
                new TemperatureValidator(),
                new ConsoleSummaryWriter(),
                new FileSummaryWriter()
        );
    }

    public TemperatureBatchProcessor(
            TemperatureLineReader fileReader,
            LineParser<TemperatureReading> csvParser,
            Validator<TemperatureReading> validator,
            ReportWriter consoleWriter,
            ReportWriter fileWriter) {
        this.fileReader = fileReader;
        this.csvParser = csvParser;
        this.validator = validator;
        this.consoleWriter = consoleWriter;
        this.fileWriter = fileWriter;
    }

    /**
     * Processes a batch of temperature readings from a CSV file.
     * Orchestrates parsing, validation, statistics, and report generation.
     * Outputs to console and writes summary to file.
     *
     * @param filename path to the input CSV file
     */
    public static void processBatch(String filename) {
        DEFAULT_PROCESSOR.process(filename);
    }

    public void process(String filename) {
        List<String> lines = readInputLines(filename);
        if (lines == null) {
            return;
        }

        ProcessingData processingData = parseAndValidate(lines);
        if (processingData.validReadings.isEmpty()) {
            System.out.println("No valid temperature data found.");
            return;
        }

        SummaryReport report = buildReport(lines.size(), processingData);
        writeOutputs(filename, report);
    }

    private List<String> readInputLines(String filename) {
        try {
            return fileReader.readLines(filename);
        } catch (IOException e) {
            System.out.println("Error: File not found.");
            return null;
        }
    }

    private ProcessingData parseAndValidate(List<String> lines) {
        List<TemperatureReading> validReadings = new ArrayList<>();
        List<String> badLines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            processLine(lines.get(i), i + 1, validReadings, badLines);
        }

        return new ProcessingData(validReadings, badLines);
    }

    private void processLine(
            String rawLine,
            int lineNumber,
            List<TemperatureReading> validReadings,
            List<String> badLines) {
        String line = rawLine.trim();
        if (line.isEmpty()) {
            return;
        }

        TemperatureReading reading;
        try {
            reading = csvParser.parse(line);
        } catch (ParseException e) {
            badLines.add(formatBadLine(lineNumber, line));
            return;
        }

        TemperatureValidator.ValidationResult validation = validator.validate(reading);
        if (!validation.isValid()) {
            badLines.add(formatBadLine(lineNumber, line));
            return;
        }

        validReadings.add(reading);
    }

    private String formatBadLine(int lineNumber, String line) {
        return "  Line " + lineNumber + ": " + line;
    }

    private SummaryReport buildReport(int totalLines, ProcessingData processingData) {
        TemperatureStatistics statistics = new TemperatureStatistics(processingData.validReadings);
        return new SummaryReport(
                totalLines,
                processingData.validReadings.size(),
                processingData.badLines.size(),
                statistics,
                processingData.badLines
        );
    }

    private void writeOutputs(String filename, SummaryReport report) {
        System.out.print(consoleWriter.format(report, filename));

        String outName = filename + "_summary.txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(outName))) {
            out.print(fileWriter.format(report, filename));
            System.out.println("Report saved to " + outName);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static class ProcessingData {
        private final List<TemperatureReading> validReadings;
        private final List<String> badLines;

        private ProcessingData(List<TemperatureReading> validReadings, List<String> badLines) {
            this.validReadings = validReadings;
            this.badLines = badLines;
        }
    }
}