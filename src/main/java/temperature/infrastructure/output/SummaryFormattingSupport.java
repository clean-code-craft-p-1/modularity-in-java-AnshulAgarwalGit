package temperature.infrastructure.output;

import temperature.domain.model.SummaryReport;

/**
 * Shared formatting helpers for report writers.
 */
final class SummaryFormattingSupport {

    private static final String TITLE = "Temperature Analysis Summary\n";
    private static final String SECTION_SEPARATOR = "------------------------------------------------------------\n";

    private SummaryFormattingSupport() {
    }

    static String build(
            SummaryReport report,
            String filename,
            String border,
            boolean includeFileLine,
            boolean blankLineBeforeInvalidLines) {
        StringBuilder sb = new StringBuilder();

        sb.append(border).append("\n");
        sb.append(TITLE);
        sb.append(border).append("\n");

        if (includeFileLine) {
            sb.append("File analyzed: ").append(filename).append("\n");
        }

        sb.append("Total readings: ").append(report.getTotalReadingCount()).append("\n");
        sb.append("Valid readings: ").append(report.getValidReadingCount()).append("\n");
        sb.append("Errors: ").append(report.getErrorCount()).append("\n");
        sb.append(SECTION_SEPARATOR);
        sb.append(String.format("Max temperature: %.2f%n", report.getStatistics().getMaxTemp()));
        sb.append(String.format("Min temperature: %.2f%n", report.getStatistics().getMinTemp()));
        sb.append(String.format("Average temperature: %.2f%n", report.getStatistics().getAvgTemp()));
        sb.append(SECTION_SEPARATOR);

        if (!report.getBadLines().isEmpty()) {
            if (blankLineBeforeInvalidLines) {
                sb.append("\n");
            }
            sb.append("Invalid lines:\n");
            for (String badLine : report.getBadLines()) {
                sb.append(badLine).append("\n");
            }
        }

        return sb.toString();
    }
}