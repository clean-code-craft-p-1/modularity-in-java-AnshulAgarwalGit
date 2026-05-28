package temperature.domain.model;

import java.util.List;

/**
 * Data model holding summary report information.
 * Clean separation between data and formatting concerns.
 */
public class SummaryReport {
    private final int totalReadingCount;
    private final int validReadingCount;
    private final int errorCount;
    private final TemperatureStatistics statistics;
    private final List<String> badLines;

    /**
     * Creates a SummaryReport from aggregate data.
     *
     * @param totalReadingCount total number of lines processed
     * @param validReadingCount number of valid readings
     * @param errorCount number of invalid lines
     * @param statistics computed temperature statistics
     * @param badLines list of formatted error line strings
     */
    public SummaryReport(
            int totalReadingCount,
            int validReadingCount,
            int errorCount,
            TemperatureStatistics statistics,
            List<String> badLines) {
        this.totalReadingCount = totalReadingCount;
        this.validReadingCount = validReadingCount;
        this.errorCount = errorCount;
        this.statistics = statistics;
        this.badLines = badLines;
    }

    public int getTotalReadingCount() {
        return totalReadingCount;
    }

    public int getValidReadingCount() {
        return validReadingCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public TemperatureStatistics getStatistics() {
        return statistics;
    }

    public List<String> getBadLines() {
        return badLines;
    }
}
