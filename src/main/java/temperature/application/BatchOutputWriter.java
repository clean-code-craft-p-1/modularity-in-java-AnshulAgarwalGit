package temperature.application;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import temperature.application.ports.ReportWriter;
import temperature.domain.model.SummaryReport;

/**
 * Writes batch results to console and summary file.
 */
class BatchOutputWriter {

    private final ReportWriter consoleWriter;
    private final ReportWriter fileWriter;

    BatchOutputWriter(ReportWriter consoleWriter, ReportWriter fileWriter) {
        this.consoleWriter = consoleWriter;
        this.fileWriter = fileWriter;
    }

    void write(String filename, SummaryReport report) {
        System.out.print(consoleWriter.format(report, filename));

        String outName = filename + "_summary.txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(outName))) {
            out.print(fileWriter.format(report, filename));
            System.out.println("Report saved to " + outName);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}
