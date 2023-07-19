package com.nhn.server.http;

import com.nhn.server.config.ServerConfig;
import com.nhn.server.exception.ForbiddenException;
import com.nhn.server.exception.NotFoundException;
import com.nhn.server.exception.NotImplementedException;
import com.nhn.server.servlet.ServletConfig;
import com.nhn.server.servlet.SimpleServlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestProcessor implements Runnable {
    private final static Logger logger = Logger.getLogger(RequestProcessor.class.getCanonicalName());
    private final Socket connection;
    private final OutputStream raw;
    private final HttpRequestParser httpRequestParser;
    private final ServerConfig serverConfig;
    private final ServletConfig servletConfig;
    private HttpRequest request;
    private HttpResponse response;

    public RequestProcessor(ServerConfig serverConfig, ServletConfig servletConfig, Socket connection) throws IOException {
        this.serverConfig = serverConfig;
        this.servletConfig = servletConfig;
        this.connection = connection;

        this.httpRequestParser = new HttpRequestParser(connection.getInputStream());
        this.raw = new BufferedOutputStream(connection.getOutputStream());
    }

    @Override
    public void run() {
        try {
            request = httpRequestParser.parse();
            response = new HttpResponse(connection);

            logger.info(connection.getRemoteSocketAddress() + " " + request.info());
            logger.info(connection.getInetAddress().getHostAddress() + " " + request.info());

            if (!request.isGetMethod()) {
                throw new NotImplementedException();
            }

            if (servletConfig.isServlet(request.uri())) {
                // do Servlet
                SimpleServlet servlet = servletConfig.getServlet(request.uri());
                servlet.service(null, null);
            } else {
                // do static
                if (request.isRootPath()) {
                    request.appendPath(serverConfig.index());
                }
                handleGetRequest(request, response);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            handleIOException(connection, ex);
        } catch (ForbiddenException e) {
            response.sendError(serverConfig.getErrorPage("403"));
        } catch (NotFoundException e) {
            response.sendError(serverConfig.getErrorPage("404"));
        } catch (NotImplementedException e) {
            response.sendError(serverConfig.getErrorPage("501"));
        } finally {
            closeConnection(connection);
        }
    }

    private void handleGetRequest(HttpRequest httpRequest, HttpResponse response) throws IOException {
        System.out.println("serverConfig = " + serverConfig.rootDirectory());
        System.out.println("httpRequest = " + httpRequest.uri());
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(serverConfig.rootDirectory() + httpRequest.uri());

        if (resourceAsStream == null) {
            throw new NotFoundException();
        }

        byte[] theData = resourceAsStream.readAllBytes();
        if (httpRequest.isHttp()) {
            response.setHeader("HTTP/1.0 200 OK", httpRequest.contentType(), theData.length);
        }
        sendFile(theData);
    }

    private void sendFile(byte[] data) throws IOException {
        raw.write(data);
        raw.flush();
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
}
