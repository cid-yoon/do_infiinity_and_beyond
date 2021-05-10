package util;

public class HttpUtils {

    static final String DEFAULT_TOKEN = " ";

    public static String[] tokenize(String token, String message) {
        return message.split(token);
    }

    public static String[] tokenize(String message) {
        return tokenize(DEFAULT_TOKEN, message);
    }

}
