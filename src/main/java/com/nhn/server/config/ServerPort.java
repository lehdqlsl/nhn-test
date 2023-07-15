package com.nhn.server.config;

import java.util.logging.Logger;

public class ServerPort {
    private static final Logger logger = Logger.getLogger(ServerPort.class.getCanonicalName());
    private final int port;

    public ServerPort(int port) {
        if (isOutOfRange(port)) {
            logger.warning("number " + port + " is invalid. The default value is set to 8080.");
            port = 8080;
        }
        this.port = port;
    }

    private boolean isOutOfRange(int port) {
        return port < 0 || port > 65535;
    }

    public int number() {
        return port;
    }
}
