package com.nhn.server.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class ServerPortTest {
    @Test
    void port() {
        assertThatCode(() -> new ServerPort(10000))
                .doesNotThrowAnyException();
    }

    @Test
    void invalid_port() {
        ServerPort serverPort = new ServerPort(-5);

        assertThat(serverPort.number()).isEqualTo(8080);
    }
}
