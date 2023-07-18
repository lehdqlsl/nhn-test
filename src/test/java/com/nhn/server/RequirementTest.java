package com.nhn.server;

import com.nhn.server.config.ConfigurationManager;
import com.nhn.server.http.HttpRequest;
import com.nhn.server.http.HttpRequestParser;
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
}
