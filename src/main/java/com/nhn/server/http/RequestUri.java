package com.nhn.server.http;

import com.nhn.server.exception.ForbiddenException;

public class RequestUri {
    private String uri;

    public RequestUri(String uri) {
        checkExtension(uri);
        this.uri = uri;
    }

    private static void checkExtension(String uri) {
        String[] split = uri.split("\\.");
        String extension = split.length > 1 ? split[1] : null;

        if (extension != null && BlockedExtension.contains(extension)) {
            throw new ForbiddenException();
        }
    }

    private void checkExtensionSecurity(String uri) {

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
}
