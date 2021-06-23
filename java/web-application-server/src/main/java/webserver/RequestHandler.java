package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import util.RequestCommand;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;


public class RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String message = br.readLine();

            if (message == null) {
                return;
            }

            String[] tokens = HttpRequestUtils.tokenize(message);
            RequestCommand command = RequestCommand.create(tokens);

            Map<String, String> headers = HttpRequestUtils.parseHeader(br);
            if ("GET".equals(command.getMethod())) {
                String queryString = HttpRequestUtils.parseQueryString(command.fullPath());
                //Map<String, String> paramMap = HttpRequestUtils.toMap(queryString);

                // 사용자 요청 처리는 이곳에서 구현
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp" + command.fullPath()).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);

            } else if ("POST".equals(command.getMethod())) {

                String contentLength = headers.get("Content-Length");
                String bodyDataString = IOUtils.readData(br, Integer.parseInt(contentLength));
                Map<String, String> paramMap = HttpRequestUtils.toMap(bodyDataString);

                User user = new User(paramMap.get("userId"), paramMap.get("password"), paramMap.get("name"), paramMap.get("email"));
                System.out.println(user);

                // 사용자 요청 처리는 이곳에서 구현
                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos);
            }


        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + "http://localhost:8888/index.html" + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
