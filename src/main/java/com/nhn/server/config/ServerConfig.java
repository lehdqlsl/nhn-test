package com.nhn.server.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ServerConfig {
    private static final Logger logger = Logger.getLogger(ServerConfig.class.getCanonicalName());
    public static final String PREFIX = "static";
    private int port;
    private String docRoot;
    private final Map<String, String> errorPages = new HashMap<>();

    public ServerConfig(ServerConfigWrapper serverConfigWrapper) throws IOException {
        setDocRoot(serverConfigWrapper.getDocRoot());
        setServerPort(serverConfigWrapper.getPort());
        setErrorPages(serverConfigWrapper.getErrorPage());
    }

    private void setErrorPages(Map<String, String> errorPages) {
        this.errorPages.put("403", "pages/403.html");
        this.errorPages.put("404", "pages/404.html");
        this.errorPages.put("500", "pages/500.html");
        this.errorPages.put("501", "pages/501.html");

        this.errorPages.putAll(errorPages);
    }

    private void setDocRoot(String docRoot) {
        if (new File(docRoot).isFile()) {
            throw new IllegalArgumentException(
                    "rootDirectory must be a directory, not a file");
        }
        this.docRoot = docRoot;
    }

    public void setServerPort(int port) {
        if (isOutOfRange(port)) {
            logger.warning("number " + port + " is invalid. The default value is set to 8080.");
            port = 8080;
        }
        this.port = port;
    }

    private boolean isOutOfRange(int port) {
        return port < 0 || port > 65535;
    }

    public String getDocRoot() {
        return docRoot;
    }

    public int getServerPort() {
        return port;
    }

    public String rootDirectory() {
        return PREFIX + docRoot;
    }
}
