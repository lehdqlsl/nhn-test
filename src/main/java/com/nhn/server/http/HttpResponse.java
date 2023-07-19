package com.nhn.server.http;

import com.nhn.server.servlet.HttpServletResponse;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class HttpResponse implements HttpServletResponse {
    private final OutputStreamWriter out;

    public HttpResponse(Socket connection) {
        try {
            this.out = new OutputStreamWriter(connection.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void setHeader(String responseCode, String contentType, int length) {
        try {
            out.write(responseCode + "\r\n");
            out.write("Date: " + new Date() + "\r\n");
            out.write("Server: HTTP 2.0\r\n");
            out.write("Content-length: " + length + "\r\n");
            out.write("Content-type: " + contentType + "\r\n\r\n");
            out.flush();
        } catch (IOException e) {

        }
    }

    @Override
    public void sendError(String errorPage) {
        try {
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(errorPage);
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String html = stringBuilder.toString();
            setHeader("HTTP/1.0 403 Forbidden", "text/html; charset=utf-8", html.length());
            out.write(html);
            out.flush();
        } catch (IOException e) {

        }
    }

    @Override
    public PrintWriter getWriter() {
        return new PrintWriter(out);
    }
}
