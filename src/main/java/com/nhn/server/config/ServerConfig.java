package com.nhn.server.config;

public class ServerConfig {
    private final ServerPort serverPort;
    private String docRoot;

    public ServerConfig(ServerPort serverPort, String docRoot) {
        this.serverPort = serverPort;
        this.docRoot = docRoot;
    }

    public String getDocRoot() {
        return docRoot;
    }

    public int getServerPort() {
        return serverPort.number();
    }
}
