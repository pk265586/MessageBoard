package utils;

/**
 * Class with static mathematical methods
 */
public class MathUtils {
    /**
     * Parses string to int; returns 0 if parsing is not successful.
     * @param value String to be parsed.
     * @return integer value represented by the argument, or 0 if parsing is not successful.
     */
    public static int tryParseInt(String value) {
        return tryParseInt(value, 0);
    }

    /**
     * Parses string to int; returns default value if parsing is not successful.
     * @param value String to be parsed.
     * @param defaultVal Default value to return if parsing is not successful.
     * @return integer value represented by the argument, or default value if parsing is not successful.
     */
    public static int tryParseInt(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
