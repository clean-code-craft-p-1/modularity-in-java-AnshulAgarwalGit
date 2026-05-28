package temperature.application.ports;

import java.io.IOException;
import java.util.List;

/**
 * Abstraction for reading raw temperature lines from a source.
 */
public interface TemperatureLineReader {
    List<String> readLines(String source) throws IOException;
}