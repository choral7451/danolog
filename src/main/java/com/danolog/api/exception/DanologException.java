package com.danolog.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class DanologException extends RuntimeException {

  public final Map<String, String> validation = new HashMap<>();

  public DanologException(String message) {
    super(message);
  }

  public DanologException(String message, Throwable cause) {
    super(message, cause);
  }

  public abstract int getStatusCode();

  public void addValidation(String fieldName, String message) {
    validation.put(fieldName, message);
  }
}
