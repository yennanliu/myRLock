package com.yen.MyRLock.handler.exception;

public class MyRlockInvocationException extends RuntimeException {

  public MyRlockInvocationException() {}

  public MyRlockInvocationException(String message) {
    super(message);
  }

  public MyRlockInvocationException(String message, Throwable cause) {
    super(message, cause);
  }
}
