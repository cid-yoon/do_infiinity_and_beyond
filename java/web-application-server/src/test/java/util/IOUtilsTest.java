package util;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class IOUtilsTest {

    private final String testDirectory ="./src/test/resources/";

    @Test
    void 버퍼에서_파일읽기_성공() throws IOException {

        String data = "abcdefg";
        StringReader stringReader = new StringReader(data);
        BufferedReader bufferedReader = new BufferedReader(stringReader);

        assertThat(data).isEqualTo(IOUtils.readData(bufferedReader, data.length()));
    }

    @Test
    void 파일에서_한줄읽기_성공() throws IOException {

        InputStream inputStream = new FileInputStream(testDirectory + "readData.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        assertThat("hello").isEqualTo(IOUtils.readData(bufferedReader, "hello".length()));
    }

    @Test
    void 파일에서_여러줄읽기_성공() throws IOException {

        List<String> dataList = Arrays.asList("hello", "world", "webApp");
        String stringLines = String.join("\n", dataList);

        StringReader stringReader = new StringReader(stringLines);
        BufferedReader bufferedReader = new BufferedReader(stringReader);

        for (int i = 0; i < dataList.size(); i++) {

            String readLine = bufferedReader.readLine();
            assertThat(dataList.get(i)).isEqualTo(readLine);
        }

        assertThat(bufferedReader.readLine()).isNullOrEmpty();

    }


}