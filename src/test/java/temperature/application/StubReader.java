package temperature.application;

import java.util.List;
import temperature.application.ports.TemperatureLineReader;

class StubReader implements TemperatureLineReader {

    private final List<String> lines;

    StubReader(List<String> lines) {
        this.lines = lines;
    }

    @Override
    public List<String> readLines(String source) {
        return lines;
    }
}
