package com.nhn.server.config;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


public class ConfigurationManagerTest {

    @Test
    void invalid_port() {
        ServerConfigWrapper wrapper = new ServerConfigWrapper(-5, "ROOT", new ArrayList<>());

        ServletConfig servletConfig = new ServletConfig(wrapper, new ArrayList<>());
        assertThat(servletConfig.port()).isEqualTo(8080);
    }
}
