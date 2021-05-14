package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtils {

    static final String COMMAND_TOKEN = " ";


    /**
     * queryString의 {key}={value} 형태의 문자를 분리하는 토큰
     */
    public static final String QUERY_STRING_KV_TOKEN = "[&]";

    /**
     * 스플릿을 사용할때 특수문자 토큰인 경우
     * \\? 구분 해 주던지
     * [?]로 감싸 주어야 동작
     * 그렇지 않으면 regex가 정상적으로 동작하지 못함
     * 하지만 index는 그냥 써도 됨
     */
    public static final char QUERY_STRING_TOKEN_CODE = '?';

    public static String[] tokenize(String token, String message) {
        return message.split(token);
    }

    public static String parseQueryString(String uri) {

        if (isEmptyString(uri)) {
            return "";
        }

        int index = uri.indexOf(QUERY_STRING_TOKEN_CODE);
        if (index == -1) {
            return "";
        }

        // String requestParam = uri.substring(0, index);
        String params = uri.substring(index + 1);

        return params;
    }

    public static String[] tokenize(String message) {
        return tokenize(COMMAND_TOKEN, message);
    }


    public static Map<String, String> toMap(String params) {

        if (isEmptyString(params)) {
            return new HashMap<>();
        }

        String[] tokens = tokenize(QUERY_STRING_KV_TOKEN, params);
        if (tokens.length == 0) {
            return new HashMap<>();
        }

        HashMap<String, String> tokenMap = new HashMap<>();
        for (String token : tokens) {

            String[] t = token.split("[=]");
            if (t.length < 2) {
                continue;
            }

            tokenMap.put(t[0], t[1]);
        }

        return tokenMap;
    }


    public static boolean isEmptyString(String message) {

        if (message == null) {
            return true;
        }

        if ("".equals(message)) {
            return true;
        }

        return false;
    }

    public static boolean isQueryString(String message) {

        if (isEmptyString(message)) {
            return false;
        }

        if (message.indexOf(QUERY_STRING_TOKEN_CODE) == -1) {
            return false;
        }

        return true;

    }

    public static Map<String, String> parseHeader(BufferedReader br) throws IOException {

        HashMap<String, String> headers = new HashMap<>();
        String message = br.readLine();
        while (!"".equals(message)) {
            String[] tokens = tokenize(": ", message);
            headers.put(tokens[0], tokens[1]);

            message = br.readLine();
        }

        return headers;

    }
}
