package com.danolog.api.controller;

import com.danolog.api.request.Login;
import com.danolog.api.response.SessionResponse;
import com.danolog.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final String KEY = "1oaAWvrgDcYmUZNqnyhJRQHDfWGkVK3Vnt4J0mn7vy0=";
  @PostMapping("/auth/login")
  public SessionResponse login(@RequestBody @Valid Login login) {
    Long userId = authService.signin(login);
    log.info(">>>>>>>", userId);
    SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));

    String jws = Jwts.builder().subject(String.valueOf(userId)).signWith(key).compact();
    log.info(">>>>>>>", jws);
    return new SessionResponse(jws);
  }
}