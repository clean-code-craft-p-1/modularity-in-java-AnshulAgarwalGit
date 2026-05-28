package temperature;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TemperatureBatchProcessor {

    private static void writeSummary(
            PrintWriter writer,
            String filename,
            int totalReadings,
            int validReadings,
            int errors,
            double maxTemp,
            double minTemp,
            double avgTemp,
            List<String> badLines,
            boolean includeFileAnalyzed,
            String headerSeparator,
            boolean blankLineBeforeInvalidLines) {
        writer.println(headerSeparator);
        writer.println("Temperature Analysis Summary");
        writer.println(headerSeparator);
        if (includeFileAnalyzed) {
            writer.println("File analyzed: " + filename);
        }
        writer.println("Total readings: " + totalReadings);
        writer.println("Valid readings: " + validReadings);
        writer.println("Errors: " + errors);
        writer.println("------------------------------------------------------------");
        writer.printf("Max temperature: %.2f%n", maxTemp);
        writer.printf("Min temperature: %.2f%n", minTemp);
        writer.printf("Average temperature: %.2f%n", avgTemp);
        writer.println("------------------------------------------------------------");

        if (!badLines.isEmpty()) {
            if (blankLineBeforeInvalidLines) {
                writer.println();
            }
            writer.println("Invalid lines:");
            for (String badLine : badLines) {
                writer.println(badLine);
            }
        }
    }

    public static void processBatch(String filename) {
        List<String> lines;

        try {
            lines = Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            System.out.println("Error: File not found.");
            return;
        }

        List<Double> temps = new ArrayList<>();
        int errors = 0;
        List<String> badLines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length != 2) {
                errors++;
                badLines.add("  Line " + (i + 1) + ": " + line);
                continue;
            }

            String timestamp = parts[0].strip();
            String value = parts[1].strip();

            if (timestamp.split(":").length != 3) {
                errors++;
                badLines.add("  Line " + (i + 1) + ": " + line);
                continue;
            }

            double temp;
            try {
                temp = Double.parseDouble(value);
            } catch (NumberFormatException e) {
                errors++;
                badLines.add("  Line " + (i + 1) + ": " + line);
                continue;
            }

            if (temp < -100 || temp > 200) {
                errors++;
                badLines.add("  Line " + (i + 1) + ": " + line);
                continue;
            }

            temps.add(temp);
        }

        if (temps.isEmpty()) {
            System.out.println("No valid temperature data found.");
            return;
        }

        double maxTemp = Collections.max(temps);
        double minTemp = Collections.min(temps);
        double avgTemp = temps.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        PrintWriter consoleWriter = new PrintWriter(System.out, true);
        writeSummary(
                consoleWriter,
                filename,
                lines.size(),
                temps.size(),
                errors,
                maxTemp,
                minTemp,
                avgTemp,
                badLines,
                false,
                "============================================================",
                false);

        String outName = filename + "_summary.txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(outName))) {
            writeSummary(
                    out,
                    filename,
                    lines.size(),
                    temps.size(),
                    errors,
                    maxTemp,
                    minTemp,
                    avgTemp,
                    badLines,
                    true,
                    "==================================================",
                    true);
            System.out.println("Report saved to " + outName);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}