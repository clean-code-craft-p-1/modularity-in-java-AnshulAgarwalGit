package temperature.app;

import temperature.application.TemperatureBatchProcessor;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java temperature.app.Main <input-file>");
            return;
        }

        TemperatureBatchProcessor processor = new TemperatureBatchProcessor();
        processor.process(args[0]);
    }
}