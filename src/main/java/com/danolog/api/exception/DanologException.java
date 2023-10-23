package com.danolog.api.exception;

public abstract class DanologException extends RuntimeException {
  public DanologException(String message) {
    super(message);
  }

  public DanologException(String message, Throwable cause) {
    super(message, cause);
  }

  public abstract int getStatusCode();
}
