package temperature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Wraps file I/O operations for reading temperature data.
 * Single responsibility: read file lines with error handling.
 */
public class TemperatureFileReader implements TemperatureLineReader {

    /**
     * Reads all lines from a file.
     *
     * @param filename path to the file to read
     * @return list of lines (excluding fully empty lines is done by caller)
     * @throws IOException if file cannot be read
     */
    public List<String> readLines(String filename) throws IOException {
        return Files.readAllLines(Paths.get(filename));
    }
}
