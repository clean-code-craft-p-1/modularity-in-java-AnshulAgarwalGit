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
                new TemperatureReading("12:00:00", 20.0),
                new TemperatureReading("12:30:00", 25.0),
                new TemperatureReading("13:00:00", 30.0)
        );

        TemperatureStatistics stats = new TemperatureStatistics(readings);

        assertEquals(20.0, stats.getMinTemp());
        assertEquals(30.0, stats.getMaxTemp());
        assertEquals(25.0, stats.getAvgTemp());
        assertEquals(3, stats.getCount());
    }

    @Test
    public void singleReadingStatistics() {
        List<TemperatureReading> readings = Arrays.asList(
                new TemperatureReading("12:00:00", 22.5)
        );

        TemperatureStatistics stats = new TemperatureStatistics(readings);

        assertEquals(22.5, stats.getMinTemp());
        assertEquals(22.5, stats.getMaxTemp());
        assertEquals(22.5, stats.getAvgTemp());
        assertEquals(1, stats.getCount());
    }

    @Test
    public void negativeTemperatures() {
        List<TemperatureReading> readings = Arrays.asList(
                new TemperatureReading("12:00:00", -10.0),
                new TemperatureReading("12:30:00", -5.0),
                new TemperatureReading("13:00:00", 0.0)
        );

        TemperatureStatistics stats = new TemperatureStatistics(readings);

        assertEquals(-10.0, stats.getMinTemp());
        assertEquals(0.0, stats.getMaxTemp());
        assertEquals(-5.0, stats.getAvgTemp());
    }

    @Test
    public void throwsExceptionForEmptyList() {
        List<TemperatureReading> readings = Arrays.asList();
        assertThrows(IllegalArgumentException.class, () -> new TemperatureStatistics(readings));
    }
}
