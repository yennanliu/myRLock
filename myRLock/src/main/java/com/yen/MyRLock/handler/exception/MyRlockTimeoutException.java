package com.yen.MyRLock.handler.exception;

public class MyRlockTimeoutException extends RuntimeException {

    public MyRlockTimeoutException() {
    }

    public MyRlockTimeoutException(String message) {
        super(message);
    }

    public MyRlockTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

}