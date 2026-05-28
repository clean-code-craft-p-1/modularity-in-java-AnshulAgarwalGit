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
        // Read file lines
        List<String> lines;
        try {
            lines = fileReader.readLines(filename);
        } catch (IOException e) {
            System.out.println("Error: File not found.");
            return;
        }

        // Parse and validate each line
        List<TemperatureReading> validReadings = new ArrayList<>();
        List<String> badLines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }

            // Try to parse the line
            TemperatureReading reading;
            try {
                reading = csvParser.parse(line);
            } catch (ParseException e) {
                badLines.add("  Line " + (i + 1) + ": " + line);
                continue;
            }

            // Try to validate the parsed reading
            TemperatureValidator.ValidationResult validation = validator.validate(reading);
            if (!validation.isValid()) {
                badLines.add("  Line " + (i + 1) + ": " + line);
                continue;
            }

            validReadings.add(reading);
        }

        // Check if we have any valid data
        if (validReadings.isEmpty()) {
            System.out.println("No valid temperature data found.");
            return;
        }

        // Compute statistics
        TemperatureStatistics statistics = new TemperatureStatistics(validReadings);

        // Build summary report
        SummaryReport report = new SummaryReport(
                lines.size(),
                validReadings.size(),
                badLines.size(),
                statistics,
                badLines
        );

        // Output to console
        String consoleOutput = consoleWriter.format(report, filename);
        System.out.print(consoleOutput);

        // Output to file
        String outName = filename + "_summary.txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(outName))) {
            String fileOutput = fileWriter.format(report, filename);
            out.print(fileOutput);
            System.out.println("Report saved to " + outName);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}