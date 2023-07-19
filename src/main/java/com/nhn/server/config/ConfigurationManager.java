package com.nhn.server.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.server.servlet.ServletMapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ConfigurationManager {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static final String CONFIG_JSON = "config.json";
    public static final String SERVLET_MAPPING_JSON = "servlet-mapping.json";

    public static ServletConfig getServerConfig() {
        InputStream serverConfig = ConfigurationManager.class.getClassLoader().getResourceAsStream(CONFIG_JSON);
        InputStream servletConfig = ConfigurationManager.class.getClassLoader().getResourceAsStream(SERVLET_MAPPING_JSON);
        try {
            List<ServletMapping> servletMappings = mapper.readValue(servletConfig, new TypeReference<>() {
            });
            ServerConfigWrapper serverConfigWrapper = mapper.readValue(serverConfig, ServerConfigWrapper.class);
            return new ServletConfig(serverConfigWrapper, servletMappings);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
