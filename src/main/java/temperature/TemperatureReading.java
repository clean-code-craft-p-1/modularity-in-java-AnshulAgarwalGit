package temperature;

/**
 * Immutable model representing a successfully parsed temperature reading.
 * Holds a timestamp string and temperature in Celsius.
 */
public class TemperatureReading {
    private final String timestamp;
    private final double celsius;

    public TemperatureReading(String timestamp, double celsius) {
        this.timestamp = timestamp;
        this.celsius = celsius;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getCelsius() {
        return celsius;
    }

    @Override
    public String toString() {
        return "TemperatureReading{" +
                "timestamp='" + timestamp + '\'' +
                ", celsius=" + celsius +
                '}';
    }
}
