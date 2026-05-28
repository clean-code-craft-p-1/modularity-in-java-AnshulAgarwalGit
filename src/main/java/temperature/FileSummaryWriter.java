package temperature;

/**
 * Formats a SummaryReport for file output.
 * Returns the formatted string without writing directly.
 */
public class FileSummaryWriter implements ReportWriter {

    /**
     * Formats a SummaryReport for file output.
     *
     * @param report the summary report to format
     * @param filename the input filename (displayed in file output)
     * @return formatted string for file output
     */
    public String format(SummaryReport report, String filename) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("==================================================\n");
        sb.append("Temperature Analysis Summary\n");
        sb.append("==================================================\n");
        sb.append("File analyzed: ").append(filename).append("\n");
        sb.append("Total readings: ").append(report.getTotalReadingCount()).append("\n");
        sb.append("Valid readings: ").append(report.getValidReadingCount()).append("\n");
        sb.append("Errors: ").append(report.getErrorCount()).append("\n");
        sb.append("------------------------------------------------------------\n");
        sb.append(String.format("Max temperature: %.2f%n", report.getStatistics().getMaxTemp()));
        sb.append(String.format("Min temperature: %.2f%n", report.getStatistics().getMinTemp()));
        sb.append(String.format("Average temperature: %.2f%n", report.getStatistics().getAvgTemp()));
        sb.append("------------------------------------------------------------\n");

        if (!report.getBadLines().isEmpty()) {
            sb.append("\n");
            sb.append("Invalid lines:\n");
            for (String badLine : report.getBadLines()) {
                sb.append(badLine).append("\n");
            }
        }

        return sb.toString();
    }
}
