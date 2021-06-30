package webserver;


public class ReadMethodLine {

    public ReadMethodLine(String method, String path, String protocol) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
    }

    private String method;
    private String path;
    private String protocol;

    public ReadMethodLine(String[] tokens) {
        this(tokens[0], tokens[1], tokens[2]);
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
}
