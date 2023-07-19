package com.nhn.server.servlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ServletTest {

    @Test
    void load_servlet_class() throws ClassNotFoundException {

        Class<?> testServlet = getClass().getClassLoader().loadClass("com.nhn.server.servlet.TestServlet");

        assertThat(testServlet.getName()).isEqualTo("com.nhn.server.servlet.TestServlet");
        assertThat(testServlet.getSimpleName()).isEqualTo("TestServlet");
    }

    @Test
    void do_servlet() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> testServlet = getClass().getClassLoader().loadClass("com.nhn.server.servlet.TestServlet");

        SimpleServlet servlet = (SimpleServlet) testServlet.getDeclaredConstructor().newInstance();

        servlet.service(null, null);
    }

    @Test
    void servlet_config_load() throws IOException {
        JsonMapper jsonMapper = new JsonMapper();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("servlet-mapping.json");
        List<ServletMapping> servletMappings = jsonMapper.readValue(inputStream, new TypeReference<>() {});

        assertThat(servletMappings).hasSize(1);
    }
}
