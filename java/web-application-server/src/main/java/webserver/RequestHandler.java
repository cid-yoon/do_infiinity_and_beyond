package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HTTPUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


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

        try(InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()){

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String readLine = bufferedReader.readLine();

            String[] tokens = HTTPUtils.methodTokenize(readLine);
            ReadMethodLine methodLine = new ReadMethodLine(tokens);

            byte[] result = Files.readAllBytes(new File("./webapp" + methodLine.getPath()).toPath());
            while (!"".equals(readLine)){

                log.info(readLine);
                readLine = bufferedReader.readLine();
            }



            // 소켓을 통해 버퍼를 읽고 쓰기
            // 출력을 위해 outputstream에 버퍼를 연결
            DataOutputStream dos = new DataOutputStream(out);

            // 헤더를 통해 바디가 얼만큼인지 설정
            response200Header(dos, result.length);

            // 바디에 전달해줄 값을 전송
            responseBody(dos, result);
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
