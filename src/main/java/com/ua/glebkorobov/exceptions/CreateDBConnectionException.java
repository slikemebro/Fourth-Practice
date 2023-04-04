package com.ua.glebkorobov.exceptions;

public class CreateDBConnectionException extends RuntimeException{
    public CreateDBConnectionException() {
    }

    public CreateDBConnectionException(String message) {
        super(message);
    }

    public CreateDBConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateDBConnectionException(Throwable cause) {
        super(cause);
    }

    public CreateDBConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
