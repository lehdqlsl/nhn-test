package com.nhn.server.http;

import com.nhn.server.servlet.HttpServletRequest;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest implements HttpServletRequest {
    private final Map<String, String> headers = new HashMap<>();
    private final String method;
    private final RequestUri uri;
    private final String version;

    HttpRequest(String startLine) {
        String[] parts = startLine.split(" ");
        this.method = parts[0];
        this.uri = new RequestUri(parts[1]);
        this.version = parts[2];
    }

    public void putHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String method() {
        return method;
    }

    @Override
    public String getUri() {
        return uri.value();
    }

    public String version() {
        return version;
    }

    @Override
    public String host() {
        return headers.get("host");
    }

    public String info() {
        return method + " " + uri + " " + version;
    }

    public boolean isGetMethod() {
        return method.equals("GET");
    }

    public void appendPath(String indexFileName) {
        uri.append(indexFileName);
    }

    public boolean isHttp() {
        return version.startsWith("HTTP/");
    }
}
