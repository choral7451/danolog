package com.danolog.api.exception;

public class InvalidSigninInformation extends DanologException {
  private static final String MESSAGE = "아이디/비밀번호가 올바르지 않습니다.";

  public InvalidSigninInformation() {
    super(MESSAGE);
  }
  public int getStatusCode() {
    return 400;
  }
}
