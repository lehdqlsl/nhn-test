package com.nhn.server.http;

import com.nhn.server.config.ServletConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {
    private static final Logger logger = Logger.getLogger(HttpServer.class.getCanonicalName());
    private final ServletConfig servletConfig;

    public HttpServer(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    public void start() throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(servletConfig.numberOfThread());
        try (ServerSocket server = new ServerSocket(servletConfig.port())) {
            process(pool, server);
        }
    }

    private void process(ExecutorService pool, ServerSocket server) {
        logger.info("Accepting connections on port " + server.getLocalPort());
        logger.info("Document Root: " + servletConfig.rootDirectory());
        while (true) {
            try {
                Socket request = server.accept();
                Runnable r = new RequestProcessor(servletConfig, request);
                pool.submit(r);
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Error accepting connection", ex);
            }
        }
    }
}
