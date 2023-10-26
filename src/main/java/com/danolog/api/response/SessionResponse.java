package com.danolog.api.response;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class SessionResponse {

  private final String accessToken;

  public SessionResponse(String accessToken) {
    this.accessToken = accessToken;
  }
}
