package temperature.application;

import java.util.List;
import temperature.application.ports.ReportWriter;
import temperature.application.ports.TemperatureLineReader;
import temperature.domain.model.SummaryReport;
import temperature.domain.model.TemperatureStatistics;

/**
 * Coordinates full batch processing from input read to report outputs.
 */
public class BatchOrchestrator {

    private final BatchInputReader inputReader;
    private final LineProcessingAccumulator accumulator;
    private final BatchOutputWriter outputWriter;

    public BatchOrchestrator(
            TemperatureLineReader fileReader,
            LineProcessor lineProcessor,
            ReportWriter consoleWriter,
            ReportWriter fileWriter) {
        this.inputReader = new BatchInputReader(fileReader);
        this.accumulator = new LineProcessingAccumulator(lineProcessor);
        this.outputWriter = new BatchOutputWriter(consoleWriter, fileWriter);
    }
    public void process(String filename) {
        List<String> lines = inputReader.read(filename);
        if (lines == null) {
            return;
        }
        ProcessingData processingData = accumulator.accumulate(lines);
        if (processingData.getValidReadings().isEmpty()) {
            System.out.println("No valid temperature data found.");
            return;
        }
        SummaryReport report = buildReport(lines.size(), processingData);
        outputWriter.write(filename, report);
    }
    private SummaryReport buildReport(int totalLines, ProcessingData processingData) {
        TemperatureStatistics statistics = new TemperatureStatistics(processingData.getValidReadings());
        return new SummaryReport(
                totalLines,
                processingData.getValidReadings().size(),
                processingData.getBadLines().size(),
                statistics,
                processingData.getBadLines());
    }
}