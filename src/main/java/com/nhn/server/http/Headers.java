package com.nhn.server.http;

import java.net.URLConnection;

public class Headers {
    private final String method;
    private String fileName;
    private final String version;

    public Headers(String headers) {
        String[] tokens = headers.split("\\s+");
        method = tokens[0];
        fileName = tokens[1];
        version = tokens[2];
    }

    public String getMethod() {
        return method;
    }

    public String getFileName() {
        return fileName;
    }

    public String version() {
        return version.length() > 2 ? version : "";
    }

    @Override
    public String toString() {
        return method + " " + fileName + " " + version;
    }

    public boolean isGetMethod() {
        return method.equals("GET");
    }

    public boolean isRootPath() {
        return fileName.endsWith("/");
    }

    public void appendPath(String indexFileName) {
        fileName += indexFileName;
    }

    public String contentType() {
        return URLConnection.getFileNameMap().getContentTypeFor(fileName);
    }

    public boolean isHttp() {
        return version.startsWith("HTTP/");
    }
}
