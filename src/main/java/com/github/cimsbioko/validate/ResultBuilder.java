package com.github.cimsbioko.validate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.github.cimsbioko.validate.Throwables.getStackTrace;
import static java.lang.System.lineSeparator;

public class ResultBuilder implements StatusHandler {

    private final List<Message> messages = new LinkedList<>();

    private void append(Message msg) {
        messages.add(msg);
    }

    public Result buildResult() {
        return new Result(hasFailed(), messages);
    }

    @Override
    public void warn(String message) {
        append(new WarnMessage(message));
    }

    @Override
    public void error(String message) {
        append(new ErrorMessage(message));
    }

    @Override
    public void error(String message, Throwable throwable) {
        append(new ExceptionMessage(message, throwable));
    }

    @Override
    public void info(String message) {
        append(new InfoMessage(message));
    }

    @Override
    public boolean hasFailed() {
        return messages.stream().anyMatch(msg -> msg instanceof ErrorMessage);
    }

    private static class Result implements com.github.cimsbioko.validate.Result {

        private final boolean failed;
        private final List<Message> messages;

        Result(boolean failed, List<Message> messages) {
            this.failed = failed;
            this.messages = Collections.unmodifiableList(messages);
        }

        @Override
        public boolean hasFailed() {
            return failed;
        }

        @Override
        public List<Message> getMessages() {
            return messages;
        }
    }
}


class InfoMessage implements Message {

    private final String message;

    InfoMessage(String msg) {
        message = msg;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public String toString() {
        return message;
    }
}


class WarnMessage implements Message {

    private final String message;

    WarnMessage(String msg) {
        message = msg;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public String toString() {
        return message;
    }
}


class ErrorMessage implements Message {

    private final String message;

    ErrorMessage(String msg) {
        message = msg;
    }

    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public String toString() {
        return message;
    }
}


class ExceptionMessage extends ErrorMessage {

    private final String stackTrace;

    ExceptionMessage(String msg, Throwable throwable) {
        super(msg);
        stackTrace = getStackTrace(throwable);
    }

    @Override
    public String toString() {
        return super.toString() + lineSeparator() + stackTrace;
    }
}