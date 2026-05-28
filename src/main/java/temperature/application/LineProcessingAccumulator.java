package temperature.application;

import java.util.ArrayList;
import java.util.List;
import temperature.domain.model.TemperatureReading;

/**
 * Aggregates line-processing results into valid readings and invalid lines.
 */
class LineProcessingAccumulator {

    private final LineProcessor lineProcessor;

    LineProcessingAccumulator(LineProcessor lineProcessor) {
        this.lineProcessor = lineProcessor;
    }

    ProcessingData accumulate(List<String> lines) {
        List<TemperatureReading> validReadings = new ArrayList<>();
        List<String> badLines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            LineResult lineResult = lineProcessor.processLine(lines.get(i), i + 1);
            if (lineResult.getReading() != null) {
                validReadings.add(lineResult.getReading());
            } else if (lineResult.getBadLine() != null) {
                badLines.add(lineResult.getBadLine());
            }
        }

        return new ProcessingData(validReadings, badLines);
    }
}
