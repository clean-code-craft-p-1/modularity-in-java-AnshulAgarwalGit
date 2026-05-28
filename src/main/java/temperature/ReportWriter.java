package temperature;

/**
 * Abstraction for formatting summary reports for a target output.
 */
public interface ReportWriter {
    String format(SummaryReport report, String filename);
}