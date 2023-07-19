package com.nhn.server.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.server.servlet.ServletConfig;
import com.nhn.server.servlet.ServletMapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ConfigurationManager {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static final String CONFIG_JSON = "config.json";
    public static final String SERVLET_MAPPING_JSON = "servlet-mapping.json";

    public static ServerConfig getServerConfig() {
        InputStream config = ConfigurationManager.class.getClassLoader().getResourceAsStream(CONFIG_JSON);
        try {
            ServerConfigWrapper serverConfigWrapper = mapper.readValue(config, ServerConfigWrapper.class);
            return new ServerConfig(serverConfigWrapper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ServletConfig getServletMapping() {
        InputStream inputStream = ConfigurationManager.class.getClassLoader().getResourceAsStream(SERVLET_MAPPING_JSON);
        try {
            List<ServletMapping> servletMappings = mapper.readValue(inputStream, new TypeReference<>() {
            });
            return new ServletConfig(servletMappings);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
