package util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class HttpUtilsTest {

    @Test
    void 지정된_토큰으로_문자열을_분해한다() {

        String[] tokens = HttpUtils.tokenize(":", "aaa:bbb:ccc");
        assertThat(tokens).isNotNull();
        assertThat(tokens).contains("aaa", "bbb", "ccc");

    }

    @ParameterizedTest
    @ValueSource(strings = {"aaa bbb ccc", "ggg ff aa"} )
    void 기본_토큰으로_문자열을_분해한다( String message ) {

        String[] tokens = HttpUtils.tokenize(message);
        assertThat(tokens).isNotNull();
        assertThat(tokens.length).isEqualTo(3);
    }

}