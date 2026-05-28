package temperature.application;

import temperature.application.ports.LineParser;
import temperature.application.ports.ReportWriter;
import temperature.application.ports.TemperatureLineReader;
import temperature.application.ports.Validator;
import temperature.domain.model.TemperatureReading;
import temperature.domain.validation.TemperatureValidator;
import temperature.infrastructure.input.CsvLineParser;
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

    private final BatchOrchestrator orchestrator;

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
        LineProcessor lineProcessor = new LineProcessor(csvParser, validator);
        this.orchestrator = new BatchOrchestrator(fileReader, lineProcessor, consoleWriter, fileWriter);
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
        orchestrator.process(filename);
    }
}