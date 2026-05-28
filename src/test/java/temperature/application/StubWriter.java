package temperature.application;

import temperature.application.ports.ReportWriter;
import temperature.domain.model.SummaryReport;

class StubWriter implements ReportWriter {

    private final String response;
    private int formatCalls;

    StubWriter(String response) {
        this.response = response;
    }

    @Override
    public String format(SummaryReport report, String filename) {
        formatCalls++;
        return response;
    }

    int getFormatCalls() {
        return formatCalls;
    }
}
