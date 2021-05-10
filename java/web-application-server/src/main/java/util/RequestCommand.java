package util;

public class RequestCommand {

    private static final int METHOD_SEQUENCE = 0;
    private static final int PATH_SEQUENCE = 1;
    private static final int PROTOCOL_SEQUENCE = 2;
    private static final int MAX_COMMAND_LENGTH = PROTOCOL_SEQUENCE;


    private final String method;
    private final String path;
    private final String protocol;

    public RequestCommand(String method, String path, String protocol) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public static RequestCommand create(String[] tokens) {

        if (tokens.length < MAX_COMMAND_LENGTH) {
            throw new IllegalArgumentException();
        }

        return new RequestCommand(tokens[METHOD_SEQUENCE], tokens[PATH_SEQUENCE], tokens[PROTOCOL_SEQUENCE]);
    }

    @Override
    public String toString() {
        return "RequestCommand{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", protocol='" + protocol + '\'' +
                '}';
    }
}
