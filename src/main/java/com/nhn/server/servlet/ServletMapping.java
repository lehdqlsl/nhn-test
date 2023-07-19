package com.nhn.server.servlet;

public class ServletMapping {
    private String urlPattern;
    private String servletClass;

    public String getUrlPattern() {
        return urlPattern;
    }

    public String getServletClass() {
        return servletClass;
    }

    public boolean isMapping(String uri) {
        return urlPattern.equals(uri);
    }
}
