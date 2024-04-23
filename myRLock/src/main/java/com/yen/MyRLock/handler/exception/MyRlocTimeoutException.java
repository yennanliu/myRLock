package com.yen.MyRLock.handler.exception;

public class MyRlocTimeoutException extends RuntimeException {

    public MyRlocTimeoutException() {
    }

    public MyRlocTimeoutException(String message) {
        super(message);
    }

    public MyRlocTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

}