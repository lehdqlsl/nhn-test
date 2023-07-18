package com.nhn.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequestParser {
    private final BufferedReader bufferedReader;

    public HttpRequestParser(InputStream inputStream) {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public HttpRequest parse() {
        HttpRequest httpRequest = null;
        String line;

        try {
            if ((line = bufferedReader.readLine()) != null) {
                httpRequest = new HttpRequest(line);
            }

            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                String[] parts = line.split(": ");
                httpRequest.putHeader(parts[0].trim().toLowerCase(), parts[1].trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpRequest;
    }
}
