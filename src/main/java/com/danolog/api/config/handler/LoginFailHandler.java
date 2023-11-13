package com.danolog.api.config.handler;

import com.danolog.api.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static jakarta.servlet.http.HttpServletResponse.*;
import static java.nio.charset.StandardCharsets.*;

@Slf4j
@RequiredArgsConstructor
public class LoginFailHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    log.error("[인증오류] 아이디 혹은 비밀번호가 올바르지 않습니다.");

    ErrorResponse errorResponse = ErrorResponse.builder()
      .code("400")
      .message("아이디 혹은 비밀번호가 올바르지 않습니다.")
      .build();

    String json = objectMapper.writeValueAsString(errorResponse);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF_8.displayName());
    response.setStatus(SC_BAD_REQUEST);
    objectMapper.writeValue(response.getWriter(), errorResponse);

  }
}
