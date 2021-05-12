package util;

import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HttpUtilsTest {

    @Test
    void 지정된_토큰으로_문자열을_분해한다() {

        String[] tokens = HttpRequestUtils.tokenize(":", "aaa:bbb:ccc");
        assertThat(tokens).isNotNull();
        assertThat(tokens).contains("aaa", "bbb", "ccc");

    }

    @ParameterizedTest
    @ValueSource(strings = {"aaa bbb ccc", "ggg ff aa"})
    void 기본_토큰으로_문자열을_분해한다(String message) {

        String[] tokens = HttpRequestUtils.tokenize(message);
        assertThat(tokens).isNotNull();
        assertThat(tokens.length).isEqualTo(3);
    }

    @Test
    void 특수문자_포함된_토큰으로_문자열_분해하기() {

        String uri = "/user/create?userId=javajigi&password=password&name=Jaesung&email=javagigi%40slipp.net";
        String[] tokens = HttpRequestUtils.tokenize("\\?", uri);
        assertThat(tokens).isNotNull();
        assertThat(tokens.length).isEqualTo(2);

    }

    @Test
    void 메시지에서_쿼리_스트링_분리하기() {

        String originQueryString = "userId=javajigi&password=password&name=Jaesung&email=javagigi%40slipp.net";
        String uri = "/user/create?" + originQueryString;
        String queryString = HttpRequestUtils.parseQueryString(uri);
        assertThat(queryString).isNotNull();
        assertThat(queryString).isEqualTo(originQueryString);

    }

    @ParameterizedTest
    @CsvSource(value = {"/user/create?userId=javajigi&password=password&name=Jaesung&email=javagigi%40slipp.net:userId=javajigi&password=password&name=Jaesung&email=javagigi%40slipp.net",
            "message:''", "msg?:''", "'':''"

    }, delimiter = ':'

    )
    void 메시지에서_쿼리_스트링_분리하기_쿼리스트링이_아니면_빈문자_반환(String uri, String result) {

        String queryString = HttpRequestUtils.parseQueryString(uri);
        assertThat(queryString).isNotNull();
        assertThat(queryString).isEqualTo(result);

    }

    @Test
    void 쿼리_스트링_문자를_맵_자료구조로_분리하기() {

        String queryString = "userId=javajigi&password=password&name=Jaesung&email=javagigi%40slipp.net";
        Map<String, String> kvMap = HttpRequestUtils.toMap(queryString);
        assertThat(kvMap.size()).isEqualTo(4);
        assertThat(kvMap).containsKey("userId");
        assertThat(kvMap).containsKey("password");
        assertThat(kvMap).containsKey("name");
        assertThat(kvMap).containsKey("email");
    }

    @Test
    void 올바르지_않은_쿼리스트링_분리시_빈_맵_자료구조_반환() {

        String queryString = "userId=";
        Map<String, String> kvMap = HttpRequestUtils.toMap(queryString);
        assertThat(kvMap.size()).isEqualTo(0);
    }

    @Test
    void 빈_문자열_검사() {

        String normalMessage = "aaa";
        String emptyString = "";
        assertThat(HttpRequestUtils.isEmptyString(normalMessage)).isFalse();
        assertThat(HttpRequestUtils.isEmptyString(emptyString)).isTrue();
        assertThat(HttpRequestUtils.isEmptyString(null)).isTrue();
    }

    @Test
    void 쿼리스트링의_파라메터를_유저정보에_체워넣기() {

        String uri = "/user/create?userId=javajigi&password=password&name=Jaesung&email=javagigi%40slipp.net";

        String params = HttpRequestUtils.parseQueryString(uri);
        Map<String, String> paramMap = HttpRequestUtils.toMap(params);
        User user = User.create(paramMap);

        assertThat(user.getUserId()).isEqualTo(paramMap.get("userId"));
        assertThat(user.getName()).isEqualTo(paramMap.get("name"));
        assertThat(user.getPassword()).isEqualTo(paramMap.get("password"));
        assertThat(user.getEmail()).isEqualTo(paramMap.get("email"));

    }


}