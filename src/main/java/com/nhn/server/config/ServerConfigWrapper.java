package com.nhn.server.config;

class ServerConfigWrapper {
    private int port = 8080;
    private String docRoot = "/";
    private String _403 = "403.html";
    private String _404 = "404.html";
    private String _500 = "500.html";

    public ServerConfigWrapper() {
    }

    public int getPort() {
        return port;
    }

    public String getDocRoot() {
        return docRoot;
    }

    public String get_403() {
        return _403;
    }

    public String get_404() {
        return _404;
    }

    public String get_500() {
        return _500;
    }
}
