package com.github.cimsbioko.validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingStatusHandler implements StatusHandler {

    private static final Logger log = LoggerFactory.getLogger(LoggingStatusHandler.class);

    private boolean failed;

    @Override
    public void warn(String message) {
        log.warn(message);
    }

    public void error(String message) {
        log.error(message);
        failed = true;
    }

    public void error(String message, Throwable throwable) {
        log.error(message, throwable);
        failed = true;
    }

    public void info(String message) {
        log.info(message);
    }

    public boolean hasFailed() {
        return failed;
    }
}
