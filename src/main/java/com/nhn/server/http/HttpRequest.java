package com.nhn.server.http;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final Map<String, String> headers = new HashMap<>();
    private final String method;
    private String uri;
    private final String version;

    HttpRequest(String startLine) {
        String[] parts = startLine.split(" ");
        this.method = parts[0];
        this.uri = parts[1];
        this.version = parts[2];
    }

    public void putHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String method() {
        return method;
    }

    public String uri() {
        return uri;
    }

    public String version() {
        return version;
    }

    public String host() {
        return headers.get("host");
    }

    public String info() {
        return method + " " + uri + " " + version;
    }

    public boolean isRootPath() {
        return uri.equals("/");
    }

    public boolean isGetMethod() {
        return method.equals("GET");
    }

    public void appendPath(String indexFileName) {
        uri += "index.html";
    }

    public boolean isHttp() {
        return version.startsWith("HTTP/");
    }

    public String contentType() {
        return URLConnection.getFileNameMap().getContentTypeFor(uri);
    }
}
