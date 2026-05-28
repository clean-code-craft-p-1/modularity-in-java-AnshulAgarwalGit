package temperature.infrastructure.output;

import java.util.Collections;
import java.util.List;
import temperature.domain.model.SummaryReport;
import temperature.domain.model.TemperatureReading;
import temperature.domain.model.TemperatureStatistics;

final class SummaryWriterTestFixtures {

    private SummaryWriterTestFixtures() {
    }

    static SummaryReport twoValidReadingsReport() {
        return report(2, readings(20.0, 30.0), Collections.emptyList());
    }

    static SummaryReport singleValidReadingReport() {
        return report(1, readings(20.0), Collections.emptyList());
    }

    static SummaryReport oneInvalidLineReport() {
        return report(2, readings(20.0), List.of("  Line 2: invalid,data"));
    }

    private static SummaryReport report(int total, List<TemperatureReading> readings, List<String> badLines) {
        TemperatureStatistics stats = new TemperatureStatistics(readings);
        return new SummaryReport(total, readings.size(), badLines.size(), stats, badLines);
    }

    private static List<TemperatureReading> readings(double... values) {
        if (values.length == 1) {
            return List.of(new TemperatureReading("12:00:00", values[0]));
        }
        return List.of(
                new TemperatureReading("12:00:00", values[0]),
                new TemperatureReading("12:30:00", values[1]));
    }
}
