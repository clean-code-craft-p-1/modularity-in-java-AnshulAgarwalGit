package temperature.application;

import temperature.application.ports.LineParser;
import temperature.domain.model.TemperatureReading;
import temperature.infrastructure.input.ParseException;

class StubParser implements LineParser<TemperatureReading> {

    private int parseCalls;

    @Override
    public TemperatureReading parse(String line) throws ParseException {
        parseCalls++;
        String[] parts = line.split(",");
        if (parts.length != 2) {
            throw new ParseException("Malformed line");
        }
        return new TemperatureReading(parts[0], Double.parseDouble(parts[1]));
    }

    int getParseCalls() {
        return parseCalls;
    }
}
