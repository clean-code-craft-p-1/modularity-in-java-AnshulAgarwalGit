package temperature.application;

/**
 * Formats invalid-line entries for summary output.
 */
class BadLineFormatter {

    String format(int lineNumber, String line) {
        return "  Line " + lineNumber + ": " + line;
    }
}
