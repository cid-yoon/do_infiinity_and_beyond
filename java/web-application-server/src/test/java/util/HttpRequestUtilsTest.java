package util;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class HttpRequestUtilsTest {

    @Test
    void http_헤더를_분리한다() throws IOException {

        String name = "Content-length";
        String value = "59";

        String inputString = name + ": " + value + "\n\n";

        InputStream is = new ByteArrayInputStream(inputString.getBytes());

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        Map<String, String> headers = HttpRequestUtils.parseHeader(br);

        assertThat(headers.containsKey(name)).isTrue();
        assertThat(headers.get(name)).isEqualTo(value);

    }

}