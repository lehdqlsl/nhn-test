package com.nhn.server.http;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestProcessor implements Runnable {
    private final static Logger logger = Logger.getLogger(RequestProcessor.class.getCanonicalName());
    private final String rootDirectory;
    private final String indexFileName;
    private final Socket connection;
    private final OutputStream raw;
    private final Writer out;
    private final BufferedReader bufferedReader;
    private final HttpRequestParser httpRequestParser;

    public RequestProcessor(String rootDirectory, String indexFileName, Socket connection) throws IOException {
        this.rootDirectory = rootDirectory;
        this.indexFileName = indexFileName;
        this.connection = connection;

        this.httpRequestParser = new HttpRequestParser(connection.getInputStream());
        this.raw = new BufferedOutputStream(connection.getOutputStream());
        this.out = new OutputStreamWriter(raw);
        this.bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    @Override
    public void run() {
        try {
            HttpRequest httpRequest = httpRequestParser.parse();
            logger.info(connection.getRemoteSocketAddress() + " " + httpRequest.info());
            logger.info(connection.getInetAddress().getHostAddress() + " " + httpRequest.info());

            if (httpRequest.isRootPath()) {
                httpRequest.appendPath(indexFileName);
            }

            if (httpRequest.isGetMethod()) {
                handleGetRequest(httpRequest);
            } else {
                handleNonGetRequest("");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            handleIOException(connection, ex);
        } finally {
            closeConnection(connection);
        }
    }

    private void handleGetRequest(HttpRequest httpRequest) throws IOException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(rootDirectory + httpRequest.uri());

        if (resourceAsStream == null) {
            handleFileNotFound(httpRequest.version());
            return;
        }

        byte[] theData = resourceAsStream.readAllBytes();
        if (httpRequest.isHttp()) {
            sendHeader("HTTP/1.0 200 OK", httpRequest.contentType(), theData.length);
        }
        sendFile(theData);
    }

    private void sendFile(byte[] data) throws IOException {
        raw.write(data);
        raw.flush();
    }

    private void handleFileNotFound(String version) throws IOException {
        String body = "<HTML>\r\n" +
                "<HEAD><TITLE>File Not Found</TITLE>\r\n" +
                "</HEAD>\r\n" +
                "<BODY>" +
                "<H1>HTTP Error 404: File Not Found</H1>\r\n" +
                "</BODY></HTML>\r\n";
        if (version.startsWith("HTTP/")) {
            sendHeader("HTTP/1.0 404 File Not Found", "text/html; charset=utf-8", body.length());
        }
        out.write(body);
        out.flush();
    }

    private void handleNonGetRequest(String version) throws IOException {
        String body = "<HTML>\r\n" +
                "<HEAD><TITLE>Not Implemented</TITLE>\r\n" +
                "</HEAD>\r\n" +
                "<BODY>" +
                "<H1>HTTP Error 501: Not Implemented</H1>\r\n" +
                "</BODY></HTML>\r\n";
        if (version.startsWith("HTTP/")) {
            sendHeader("HTTP/1.0 501 Not Implemented", "text/html; charset=utf-8", body.length());
        }
        out.write(body);
        out.flush();
    }

    private void handleIOException(Socket connection, IOException ex) {
        logger.log(Level.WARNING, "Error talking to " + connection.getRemoteSocketAddress(), ex);
    }

    private void closeConnection(Socket connection) {
        try {
            connection.close();
        } catch (IOException ex) {
            // Ignore exception
        }
    }


    private void sendHeader(String responseCode, String contentType, int length)
            throws IOException {
        out.write(responseCode + "\r\n");
        Date now = new Date();
        out.write("Date: " + now + "\r\n");
        out.write("Server: HTTP 2.0\r\n");
        out.write("Content-length: " + length + "\r\n");
        out.write("Content-type: " + contentType + "\r\n\r\n");
        out.flush();
    }
}
