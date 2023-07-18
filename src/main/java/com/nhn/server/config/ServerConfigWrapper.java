package com.nhn.server.config;

import java.util.List;
import java.util.Map;

class ServerConfigWrapper {
    private int port = 8080;
    private String docRoot = "ROOT";
    private List<VirtualHost> virtualHosts;

    ServerConfigWrapper() {
    }

    public int getPort() {
        return port;
    }

    public List<VirtualHost> getVirtualHosts() {
        return virtualHosts;
    }



    public String getDocRoot() {
        return docRoot;
    }
}
