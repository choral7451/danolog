package com.danolog.api.controller;

import com.danolog.api.domain.User;
import com.danolog.api.exception.InvalidSigninInformation;
import com.danolog.api.request.Login;
import com.danolog.api.response.SessionResponse;
import com.danolog.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/auth/login")
  public SessionResponse login(@RequestBody @Valid Login login) {
    String accessToken = authService.signin(login);
    return new SessionResponse(accessToken);
  }
}
