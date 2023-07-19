package com.nhn.server.http;

import com.nhn.server.exception.ForbiddenException;

public class RequestUri {
    private String uri;

    public RequestUri(String uri) {
        checkExtension(uri);
        if (isRoot(uri)) {
            uri += "index.html";
        }
        this.uri = uri;
    }

    private boolean isRoot(String uri) {
        return uri.equals("/");
    }

    private void checkExtension(String uri) {
        String[] split = uri.split("\\.");
        String extension = split.length > 1 ? split[1] : null;

        if (extension != null && BlockedExtension.contains(extension)) {
            throw new ForbiddenException();
        }
    }

    public String value() {
        return uri;
    }

    public void append(String indexFileName) {
        this.uri += indexFileName;
    }

    @Override
    public String toString() {
        return uri;
    }

    private boolean isRoot() {
        return isRoot(uri);
    }
}
