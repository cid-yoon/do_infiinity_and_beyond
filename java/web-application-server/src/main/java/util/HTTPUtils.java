package util;

public class HTTPUtils {

    static private final String PROTOCOL_TOKENIZE_CHARACTER = " ";

    public static String[] methodTokenize(String readData) {

        if(readData == null || readData.isEmpty()){
            return new String[0];
        }

        return readData.split(PROTOCOL_TOKENIZE_CHARACTER);
    }

}
