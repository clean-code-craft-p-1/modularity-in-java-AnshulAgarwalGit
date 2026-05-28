package temperature.infrastructure.output;

import temperature.application.ports.ReportWriter;
import temperature.domain.model.SummaryReport;

/**
 * Formats a SummaryReport for console output.
 * Returns the formatted string without printing directly.
 */
public class ConsoleSummaryWriter implements ReportWriter {

    private static final String CONSOLE_BORDER = "============================================================";

    /**
     * Formats a SummaryReport for console display.
     *
     * @param report the summary report to format
     * @param filename the input filename (not displayed in console output)
     * @return formatted string for console output
     */
    public String format(SummaryReport report, String filename) {
        return SummaryFormattingSupport.build(report, filename, CONSOLE_BORDER, false, false);
    }
}
