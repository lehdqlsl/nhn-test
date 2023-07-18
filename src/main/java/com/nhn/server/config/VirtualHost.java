package com.nhn.server.config;

import java.util.Map;

public class VirtualHost {
    private String name;

    private String docRoot;

    private Map<String, String> errorPages;

    public String getName() {
        return name;
    }

    public String getDocRoot() {
        return docRoot;
    }

    public Map<String, String> getErrorPages() {
        return errorPages;
    }
}