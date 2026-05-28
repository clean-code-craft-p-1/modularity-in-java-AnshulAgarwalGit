package temperature.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TemperatureStatistics.
 * Validates aggregate calculations on known data.
 */
public class TemperatureStatisticsTest {

    @Test
    public void calculatesMinMaxAverage() {
        List<TemperatureReading> readings = Arrays.asList(
                new TemperatureReading("12:00:00", 98.0),
                new TemperatureReading("12:30:00", 99.0),
                new TemperatureReading("13:00:00", 100.0)
        );

        TemperatureStatistics stats = new TemperatureStatistics(readings);

        assertEquals(98.0, stats.getMinTemp());
        assertEquals(100.0, stats.getMaxTemp());
        assertEquals(99.0, stats.getAvgTemp());
        assertEquals(3, stats.getCount());
    }

    @Test
    public void singleReadingStatistics() {
        List<TemperatureReading> readings = Arrays.asList(
                new TemperatureReading("12:00:00", 98.6)
        );

        TemperatureStatistics stats = new TemperatureStatistics(readings);

        assertEquals(98.6, stats.getMinTemp());
        assertEquals(98.6, stats.getMaxTemp());
        assertEquals(98.6, stats.getAvgTemp());
        assertEquals(1, stats.getCount());
    }

    @Test
    public void variedHumanTemperatures() {
        List<TemperatureReading> readings = Arrays.asList(
                new TemperatureReading("12:00:00", 95.2),
                new TemperatureReading("12:30:00", 98.4),
                new TemperatureReading("13:00:00", 101.6)
        );

        TemperatureStatistics stats = new TemperatureStatistics(readings);

        assertEquals(95.2, stats.getMinTemp());
        assertEquals(101.6, stats.getMaxTemp());
        assertEquals(98.4, stats.getAvgTemp(), 0.0001);
    }

    @Test
    public void throwsExceptionForEmptyList() {
        List<TemperatureReading> readings = Arrays.asList();
        assertThrows(IllegalArgumentException.class, () -> new TemperatureStatistics(readings));
    }
}
