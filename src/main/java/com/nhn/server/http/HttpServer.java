package com.nhn.server.http;

import com.nhn.server.config.ServerConfig;
import com.nhn.server.servlet.ServletConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {
    private static final Logger logger = Logger.getLogger(HttpServer.class.getCanonicalName());
    private final ServerConfig serverConfig;
    private final ServletConfig servletConfig;

    public HttpServer(ServerConfig serverConfig, ServletConfig servletConfig) {
        this.serverConfig = serverConfig;
        this.servletConfig = servletConfig;
    }

    public void start() throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(serverConfig.numberOfThread());
        try (ServerSocket server = new ServerSocket(serverConfig.port())) {
            process(pool, server);
        }
    }

    private void process(ExecutorService pool, ServerSocket server) {
        logger.info("Accepting connections on port " + server.getLocalPort());
        logger.info("Document Root: " + serverConfig.rootDirectory());
        while (true) {
            try {
                Socket request = server.accept();
                Runnable r = new RequestProcessor(serverConfig, servletConfig, request);
                pool.submit(r);
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Error accepting connection", ex);
            }
        }
    }
}
