package com.nhn.server.http;


import com.nhn.server.config.ConfigurationManager;
import com.nhn.server.config.ServerConfig;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cybaek on 15. 5. 22..
 */
public class ApplicationRunner {
    private static final Logger logger = Logger.getLogger(ApplicationRunner.class.getCanonicalName());

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
