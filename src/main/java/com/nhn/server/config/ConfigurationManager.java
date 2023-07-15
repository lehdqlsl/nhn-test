package com.nhn.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class ConfigurationManager {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static ServerConfig getServerConfig() {
        InputStream config = ConfigurationManager.class.getClassLoader().getResourceAsStream("config.json");
        try {
            ServerConfigWrapper serverConfigWrapper = mapper.readValue(config, ServerConfigWrapper.class);
            return new ServerConfig(
                    new ServerPort(serverConfigWrapper.getPort()),
                    serverConfigWrapper.getDocRoot()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
