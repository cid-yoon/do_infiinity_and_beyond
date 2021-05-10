package util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestCommandTest {

    @Test
    void 문자배열을_사용하여_요청명령_생성() {

        String[] tokens = {"aaa", "bbb", "ccc"};
        RequestCommand command = RequestCommand.create(tokens);
        assertThat(command.getMethod()).isNotEmpty();
        assertThat(command.getMethod()).isNotBlank();
        assertThat(command.getMethod()).isEqualTo("aaa");

        assertThat(command.getPath()).isNotEmpty();
        assertThat(command.getPath()).isNotBlank();
        assertThat(command.getPath()).isEqualTo("bbb");

        assertThat(command.getProtocol()).isNotEmpty();
        assertThat(command.getProtocol()).isNotBlank();
        assertThat(command.getProtocol()).isEqualTo("ccc");

        assertThat(command.toString()).contains(tokens);

    }


}