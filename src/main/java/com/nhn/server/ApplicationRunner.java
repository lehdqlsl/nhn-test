package com.nhn.server;


import com.nhn.server.config.ConfigurationManager;
import com.nhn.server.config.ServerConfig;
import com.nhn.server.http.HttpServer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cybaek on 15. 5. 22..
 */
public class ApplicationRunner {
    private static final Logger logger = Logger.getLogger(ApplicationRunner.class.getCanonicalName());


    public ApplicationRunner(File rootDirectory, int port) throws IOException {
        if (!rootDirectory.isDirectory()) {
            throw new IOException(rootDirectory
                    + " does not exist as a directory");
        }
//        this.rootDirectory = rootDirectory;
//        this.port = port;
    }

    public static void run(Class<?> primarySource, String[] args) {
        ServerConfig serverConfig = ConfigurationManager.getServerConfig();
        try {
            HttpServer webserver = new HttpServer(serverConfig);
            webserver.start();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Server could not start", ex);
        }
    }
}
