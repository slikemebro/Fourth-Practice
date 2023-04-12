package com.ua.glebkorobov.exceptions;

public class FillingGoodsException extends RuntimeException{
    public FillingGoodsException() {

    }

    public FillingGoodsException(String message) {
        super(message);
    }

    public FillingGoodsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FillingGoodsException(Throwable cause) {
        super(cause);
    }

    public FillingGoodsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
