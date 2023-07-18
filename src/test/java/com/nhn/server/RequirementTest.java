package com.nhn.server;

import com.nhn.server.config.ConfigurationManager;
import com.nhn.server.config.ServerConfig;
import com.nhn.server.http.HttpRequest;
import com.nhn.server.http.HttpRequestParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class RequirementTest {


    @Test
    void parse_host_header() {
        InputStream inputStream = ConfigurationManager.class.getClassLoader().getResourceAsStream("headers.txt");
        HttpRequestParser httpRequestParser = new HttpRequestParser(inputStream);
        HttpRequest parse = httpRequestParser.parse();

        assertThat(parse.method()).isEqualTo("GET");
        assertThat(parse.uri()).isEqualTo("/");
        assertThat(parse.version()).isEqualTo("HTTP/1.1");
        assertThat(parse.host()).isEqualTo("localhost:8080");
    }

    @Test
    void configuration_file() {
        ServerConfig serverConfig = ConfigurationManager.getServerConfig();

        Assertions.assertThat(serverConfig.rootDirectory()).isEqualTo("webapps/ROOT");
        Assertions.assertThat(serverConfig.port()).isEqualTo(8000);
        Assertions.assertThat(serverConfig.virtualHosts()).isNotNull();
        Assertions.assertThat(serverConfig.virtualHosts()).hasSize(1);
    }
}
