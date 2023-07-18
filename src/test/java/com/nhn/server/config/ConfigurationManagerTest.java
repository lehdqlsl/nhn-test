package com.nhn.server.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;


public class ConfigurationManagerTest {

    @Test
    void read_config_file() {
        assertThatCode(() -> ConfigurationManager.getServerConfig())
                .doesNotThrowAnyException();
    }

    @Test
    void mapping_object() {
        ServerConfig serverConfig = ConfigurationManager.getServerConfig();

        assertThat(serverConfig.getDocRoot()).isNotNull();
        assertThat(serverConfig.port()).isEqualTo(8000);
    }
}
