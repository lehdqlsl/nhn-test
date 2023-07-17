package com.nhn.server.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

class ServerConfigWrapper {
    public static final String PAGES = "pages/";
    private int port = 8080;
    private String docRoot = "/";

    @JsonProperty("error_page")
    private Map<String, String> errorPage = new HashMap<>();

    public ServerConfigWrapper() {
    }

    public int getPort() {
        return port;
    }

    public String getDocRoot() {
        return docRoot;
    }

    public Map<String, String> getErrorPage() {
        return errorPage;
    }
}
