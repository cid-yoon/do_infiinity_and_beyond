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

        assertThat(command.fullPath()).isNotEmpty();
        assertThat(command.fullPath()).isNotBlank();
        assertThat(command.fullPath()).isEqualTo("bbb");

        assertThat(command.protocol()).isNotEmpty();
        assertThat(command.protocol()).isNotBlank();
        assertThat(command.protocol()).isEqualTo("ccc");

        assertThat(command.toString()).contains(tokens);

    }


}