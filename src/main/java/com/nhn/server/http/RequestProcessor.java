package com.nhn.server.http;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestProcessor implements Runnable {
    private final static Logger logger = Logger.getLogger(RequestProcessor.class.getCanonicalName());
    private File rootDirectory;
    private String indexFileName = "index.html";
    private Socket connection;

    public RequestProcessor(File rootDirectory, String indexFileName, Socket connection) {
        if (rootDirectory.isFile()) {
            throw new IllegalArgumentException(
                    "rootDirectory must be a directory, not a file");
        }
        try {
            rootDirectory = rootDirectory.getCanonicalFile();
        } catch (IOException ex) {
        }
        this.rootDirectory = rootDirectory;
        if (indexFileName != null)
            this.indexFileName = indexFileName;
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            OutputStream raw = new BufferedOutputStream(connection.getOutputStream());
            Writer out = new OutputStreamWriter(raw);
            Reader in = new InputStreamReader(new BufferedInputStream(connection.getInputStream()), "UTF-8");

            StringBuilder requestLine = readRequestLine(in);
            String get = requestLine.toString();
            logger.info(connection.getRemoteSocketAddress() + " " + get);
            logger.info(connection.getInetAddress().getHostAddress() + " " + get);

            String[] tokens = get.split("\\s+");
            String method = tokens[0];
            String version = "";
            if (method.equals("GET")) {
                handleGetRequest(tokens, version, rootDirectory, indexFileName, out, raw);
            } else {
                handleNonGetRequest(version, out);
            }
        } catch (IOException ex) {
            handleIOException(connection, ex);
        } finally {
            closeConnection(connection);
        }
    }

    private StringBuilder readRequestLine(Reader in) throws IOException {
        StringBuilder requestLine = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            if (c == '\r' || c == '\n')
                break;
            requestLine.append((char) c);
        }
        return requestLine;
    }

    private void handleGetRequest(String[] tokens, String version, File rootDirectory, String indexFileName, Writer out, OutputStream raw) throws IOException {
        String fileName = tokens[1];
        if (fileName.endsWith("/"))
            fileName += indexFileName;
        String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
        if (tokens.length > 2) {
            version = tokens[2];
        }
        File theFile = new File(rootDirectory, fileName.substring(1));
        if (isFileReadable(theFile, rootDirectory)) {
            byte[] theData = readAllBytes(theFile);
            if (version.startsWith("HTTP/")) {
                sendHeader(out, "HTTP/1.0 200 OK", contentType, theData.length);
            }
            sendFile(raw, theData);
        } else {
            handleFileNotFound(version, out);
        }
    }

    private boolean isFileReadable(File file, File rootDirectory) throws IOException {
        System.out.println("file.canRead() = " + file.canRead());
        System.out.println("file.getCanonicalPath() = " + file.getCanonicalPath());
        System.out.println("rootDirectory = " + rootDirectory.getPath());
        return file.canRead() && file.getCanonicalPath().startsWith(rootDirectory.getPath());
    }

    private byte[] readAllBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    private void sendFile(OutputStream raw, byte[] data) throws IOException {
        raw.write(data);
        raw.flush();
    }

    private void handleFileNotFound(String version, Writer out) throws IOException {
        String body = "<HTML>\r\n" +
                "<HEAD><TITLE>File Not Found</TITLE>\r\n" +
                "</HEAD>\r\n" +
                "<BODY>" +
                "<H1>HTTP Error 404: File Not Found</H1>\r\n" +
                "</BODY></HTML>\r\n";
        if (version.startsWith("HTTP/")) {
            sendHeader(out, "HTTP/1.0 404 File Not Found", "text/html; charset=utf-8", body.length());
        }
        out.write(body);
        out.flush();
    }

    private void handleNonGetRequest(String version, Writer out) throws IOException {
        String body = "<HTML>\r\n" +
                "<HEAD><TITLE>Not Implemented</TITLE>\r\n" +
                "</HEAD>\r\n" +
                "<BODY>" +
                "<H1>HTTP Error 501: Not Implemented</H1>\r\n" +
                "</BODY></HTML>\r\n";
        if (version.startsWith("HTTP/")) {
            sendHeader(out, "HTTP/1.0 501 Not Implemented", "text/html; charset=utf-8", body.length());
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


    private void sendHeader(Writer out, String responseCode, String contentType, int length)
            throws IOException {
        out.write(responseCode + "\r\n");
        Date now = new Date();
        out.write("Date: " + now + "\r\n");
        out.write("Server: JHTTP 2.0\r\n");
        out.write("Content-length: " + length + "\r\n");
        out.write("Content-type: " + contentType + "\r\n\r\n");
        out.flush();
    }
}
