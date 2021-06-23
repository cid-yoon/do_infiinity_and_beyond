package webserver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    private static final int DEFAULT_PORT = 8080;
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);

    public static void main(String[] args) throws IOException {

        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버 소켓을 생성한다 웹 서버는 기본적으로 8080번 포트를 사용
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될 때까지 대기
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection);
                requestHandler.run();
            }
        }

    }

}
