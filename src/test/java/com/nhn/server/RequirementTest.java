package com.nhn.server;

import com.nhn.server.config.ConfigurationManager;
import com.nhn.server.config.ServletConfig;
import com.nhn.server.exception.ForbiddenException;
import com.nhn.server.http.HttpRequest;
import com.nhn.server.http.HttpRequestParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class RequirementTest {


    @Test
    void parse_host_header() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("headers.txt");
        HttpRequestParser httpRequestParser = new HttpRequestParser(inputStream);
        HttpRequest parse = httpRequestParser.parse();

        assertThat(parse.method()).isEqualTo("GET");
        assertThat(parse.getUri()).isEqualTo("/");
        assertThat(parse.version()).isEqualTo("HTTP/1.1");
        assertThat(parse.host()).isEqualTo("localhost:8080");
    }

    @Test
    void configuration_file() {
        ServletConfig servletConfig = ConfigurationManager.getServerConfig();

        Assertions.assertThat(servletConfig.rootDirectory()).isEqualTo("webapps/ROOT");
        Assertions.assertThat(servletConfig.port()).isEqualTo(8000);
        Assertions.assertThat(servletConfig.virtualHosts()).isNotNull();
        Assertions.assertThat(servletConfig.virtualHosts()).hasSize(1);
    }

    @Test
    void request_exe() {
        InputStream inputStream = ConfigurationManager.class.getClassLoader().getResourceAsStream("request_exe.txt");
        HttpRequestParser httpRequestParser = new HttpRequestParser(inputStream);

        assertThatThrownBy(httpRequestParser::parse)
                .isInstanceOf(ForbiddenException.class);
    }


    @Test
    void http_error_403() {
        ServletConfig servletConfig = ConfigurationManager.getServerConfig();

        String forbidden = servletConfig.forbidden();

        assertThat(forbidden).isEqualTo("webapps/ROOT/403.html");
    }

    @Test
    void http_error_403_with_virtualhost() {
        ServletConfig servletConfig = ConfigurationManager.getServerConfig();

        String forbidden = servletConfig.forbidden("example.com");

        assertThat(forbidden).isEqualTo("webapps/example/403.html");
    }
}
