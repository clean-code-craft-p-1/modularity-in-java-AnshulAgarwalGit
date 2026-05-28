package temperature.domain.model;

/**
 * Immutable model representing a successfully parsed temperature reading.
 * Holds a timestamp string and temperature in Fahrenheit.
 */
public class TemperatureReading {
    private final String timestamp;
    private final double fahrenheit;

    public TemperatureReading(String timestamp, double fahrenheit) {
        this.timestamp = timestamp;
        this.fahrenheit = fahrenheit;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getFahrenheit() {
        return fahrenheit;
    }

    @Override
    public String toString() {
        return "TemperatureReading{" +
                "timestamp='" + timestamp + '\'' +
            ", fahrenheit=" + fahrenheit +
                '}';
    }
}
