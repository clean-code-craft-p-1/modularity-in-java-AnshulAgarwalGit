package temperature.application;

import java.io.IOException;
import java.util.List;
import temperature.application.ports.TemperatureLineReader;

/**
 * Handles input loading and maps IO failures to user-facing messages.
 */
class BatchInputReader {

    private final TemperatureLineReader fileReader;

    BatchInputReader(TemperatureLineReader fileReader) {
        this.fileReader = fileReader;
    }

    List<String> read(String filename) {
        try {
            return fileReader.readLines(filename);
        } catch (IOException e) {
            System.out.println("Error: File not found.");
            return null;
        }
    }
}
