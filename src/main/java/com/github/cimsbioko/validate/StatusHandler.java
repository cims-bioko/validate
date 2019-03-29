package com.github.cimsbioko.validate;

public interface StatusHandler {

    void warn(String message);

    void error(String message);

    void error(String message, Throwable throwable);

    void info(String message);

    boolean hasFailed();

}