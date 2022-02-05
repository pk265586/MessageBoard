package utils;

public class MathUtils {
    public static int tryParseInt(String value){
        return tryParseInt(value, 0);
    }

    public static int tryParseInt(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
