package com.nhn.server.http;

import com.nhn.server.config.ServletConfig;
import com.nhn.server.exception.ForbiddenException;
import com.nhn.server.exception.NotFoundException;
import com.nhn.server.exception.NotImplementedException;
import com.nhn.server.servlet.DefaultServlet;
import com.nhn.server.servlet.SimpleServlet;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class RequestProcessor implements Runnable {
    private final static Logger logger = Logger.getLogger(RequestProcessor.class.getCanonicalName());
    private final Socket connection;
    private final HttpRequestParser httpRequestParser;
    private final ServletConfig servletConfig;
    private HttpRequest request;
    private HttpResponse response;

    public RequestProcessor(ServletConfig servletConfig, Socket connection) throws IOException {
        this.servletConfig = servletConfig;
        this.connection = connection;
        this.httpRequestParser = new HttpRequestParser(connection.getInputStream());
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

            SimpleServlet servlet;
            if (servletConfig.isServlet(request.getUri())) {
                servlet = servletConfig.getServlet(request.getUri());
            } else {
                if (request.isRootPath()) {
                    request.appendPath(servletConfig.index());
                }
                servlet = new DefaultServlet();
            }
            servlet.init(servletConfig);
            servlet.service(request, response);
        } catch (ForbiddenException e) {
            response.sendError(servletConfig.getErrorPage("403"));
        } catch (NotFoundException e) {
            response.sendError(servletConfig.getErrorPage("404"));
        } catch (NotImplementedException e) {
            response.sendError(servletConfig.getErrorPage("501"));
        } finally {
            closeConnection(connection);
        }
    }

    private void closeConnection(Socket connection) {
        try {
            connection.close();
        } catch (IOException ex) {
            // Ignore exception
        }
    }
}
