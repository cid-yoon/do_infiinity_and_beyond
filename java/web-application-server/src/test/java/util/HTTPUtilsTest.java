package util;

import org.junit.jupiter.api.Test;
import webserver.ReadMethodLine;

import static org.assertj.core.api.Assertions.assertThat;

class HTTPUtilsTest {

    @Test
    void 메소드의_공백문자_분리하기(){

        String methodLine = "GET /index.html HTTP/1.1";

        String[] tokens = HTTPUtils.methodTokenize(methodLine);

        assertThat(tokens.length).isEqualTo(3);

    }

    @Test
    void 입력값이_NULL이면_빈배열을_반환(){

        String methodLine = null;

        String[] tokens = HTTPUtils.methodTokenize(methodLine);

        assertThat(tokens).isNotNull();
        assertThat(tokens).isEmpty();

    }

    @Test
    void 입력값에서_메소드정보와_경로정보_프로토콜_정보를_생성한다(){

        String methodLine = "GET /index.html HTTP/1.1";
        String[] tokens = HTTPUtils.methodTokenize(methodLine);

        ReadMethodLine readLine = new ReadMethodLine(tokens);
        assertThat(readLine).isNotNull();

        assertThat(readLine.getMethod()).isEqualTo("GET");
        assertThat(readLine.getPath()).isEqualTo("/index.html");
        assertThat(readLine.getProtocol()).isEqualTo("HTTP/1.1");


    }

}