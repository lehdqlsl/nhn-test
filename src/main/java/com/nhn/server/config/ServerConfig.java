package com.nhn.server.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ServerConfig {
    private static final Logger logger = Logger.getLogger(ServerConfig.class.getCanonicalName());
    private int port;
    private String docRoot;
    private Map<String, String> errorPages = new HashMap<>();
    private List<VirtualHost> virtualHosts;

    public ServerConfig(ServerConfigWrapper serverConfigWrapper) {
        setDocRoot(serverConfigWrapper.getDocRoot());
        setServerPort(serverConfigWrapper.getPort());
        setVirtualHosts(serverConfigWrapper.getVirtualHosts());
        setErrorPages();
    }

    private void setVirtualHosts(List<VirtualHost> virtualHosts) {
        virtualHosts.forEach(virtualHost -> {
            if (!virtualHost.isRootFolder()) {
                throw new IllegalArgumentException(
                        "rootDirectory must be a directory, not a file");
            }
        });
        this.virtualHosts = virtualHosts;
    }

    private void setErrorPages() {
        this.errorPages.put("403", "/403.html");
        this.errorPages.put("404", "/404.html");
        this.errorPages.put("500", "/500.html");
        this.errorPages.put("501", "/501.html");
//
//        this.errorPages.putAll(errorPages);
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

    public String rootDirectory(String host) {
        return virtualHosts.stream()
                .filter(virtualHost -> virtualHost.containHost(host))
                .map(VirtualHost::getDocRoot)
                .findFirst()
                .orElse(docRoot);
    }

    public String rootDirectory() {
        return docRoot;
    }

    public int port() {
        return port;
    }

    public List<VirtualHost> virtualHosts() {
        return virtualHosts;
    }

    public int numberOfThread() {
        return 50;
    }

    public String index() {
        return "index.html";
    }

    public String forbidden() {
        return rootDirectory() + errorPages.get("403");
    }

    public String forbidden(String host) {
        return rootDirectory(host) + errorPages.get("403");
    }
}
