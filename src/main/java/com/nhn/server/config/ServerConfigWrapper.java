package com.nhn.server.config;

import java.util.List;

class ServerConfigWrapper {
    private int port = 8080;
    private String docRoot = "webapps/ROOT";
    private List<VirtualHost> virtualHosts;

    ServerConfigWrapper(int port, String docRoot, List<VirtualHost> virtualHosts) {
        this.port = port;
        this.docRoot = docRoot;
        this.virtualHosts = virtualHosts;
    }

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
