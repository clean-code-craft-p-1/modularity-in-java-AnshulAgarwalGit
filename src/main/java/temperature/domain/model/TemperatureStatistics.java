package temperature.domain.model;

import java.util.Collections;
import java.util.List;

/**
 * Encapsulates temperature statistics calculations.
 * Takes a list of validated TemperatureReading objects and computes aggregate metrics.
 */
public class TemperatureStatistics {
    private final double minTemp;
    private final double maxTemp;
    private final double avgTemp;
    private final int count;

    /**
     * Creates TemperatureStatistics from a list of valid temperature readings.
     * Precondition: list must not be empty.
     *
     * @param readings list of validated TemperatureReading objects
     */
    public TemperatureStatistics(List<TemperatureReading> readings) {
        if (readings.isEmpty()) {
            throw new IllegalArgumentException("Cannot calculate statistics on empty list");
        }

        this.count = readings.size();
        this.maxTemp = Collections.max(readings.stream().map(TemperatureReading::getCelsius).toList());
        this.minTemp = Collections.min(readings.stream().map(TemperatureReading::getCelsius).toList());
        this.avgTemp = readings.stream()
                .mapToDouble(TemperatureReading::getCelsius)
                .average()
                .orElse(0.0);
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getAvgTemp() {
        return avgTemp;
    }

    public int getCount() {
        return count;
    }
}
