package temperature.infrastructure.output;

import temperature.application.ports.ReportWriter;
import temperature.domain.model.SummaryReport;

/**
 * Formats a SummaryReport for file output.
 * Returns the formatted string without writing directly.
 */
public class FileSummaryWriter implements ReportWriter {

    private static final String FILE_BORDER = "==================================================";

    /**
     * Formats a SummaryReport for file output.
     *
     * @param report the summary report to format
     * @param filename the input filename (displayed in file output)
     * @return formatted string for file output
     */
    public String format(SummaryReport report, String filename) {
        return SummaryFormattingSupport.build(report, filename, FILE_BORDER, true, true);
    }
}
