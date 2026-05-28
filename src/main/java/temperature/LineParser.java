package temperature;

/**
 * Abstraction for parsing a single input line into a domain object.
 */
public interface LineParser<T> {
    T parse(String line) throws ParseException;
}