package com.nhn.server.http;

import com.nhn.server.config.ServerConfig;

import java.io.File;
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
    private final int NUM_THREADS = 50;
    private final String INDEX_FILE = "index.html";

    public HttpServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void start() throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
        try (ServerSocket server = new ServerSocket(serverConfig.getServerPort())) {
            process(pool, server);
        }
    }

    private void process(ExecutorService pool, ServerSocket server) {
        logger.info("Accepting connections on port " + server.getLocalPort());
        logger.info("Document Root: " + serverConfig.rootDirectory());
        while (true) {
            try {
                Socket request = server.accept();
                Runnable r = new RequestProcessor(serverConfig.rootDirectory(), INDEX_FILE, request);
                pool.submit(r);
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Error accepting connection", ex);
            }
        }
    }
}
