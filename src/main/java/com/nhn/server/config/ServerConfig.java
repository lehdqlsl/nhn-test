package com.nhn.server.config;

import java.io.File;
import java.net.URL;

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

    public File rootDirectory() {
        System.out.println(getClass().getClassLoader().getResource("static" + docRoot));
        URL docRootUrl = getClass().getClassLoader().getResource("static" + docRoot);
        System.out.println(docRootUrl.getPath());
        return new File(docRootUrl.getFile());
    }
}
