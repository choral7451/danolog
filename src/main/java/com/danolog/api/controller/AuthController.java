package com.danolog.api.controller;

import com.danolog.api.config.AppConfig;
import com.danolog.api.request.Login;
import com.danolog.api.request.Signup;
import com.danolog.api.response.SessionResponse;
import com.danolog.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final AppConfig appConfig;

  @PostMapping("/auth/login")
  public SessionResponse login(@RequestBody @Valid Login login) {
    Long userId = authService.signin(login);

    // base64 key 생성
//    SecretKey key = Jwts.SIG.HS256.key().build();
//    Base64.getEncoder().encode(key.getEncoded());

    SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

    String jws = Jwts.builder()
      .subject(String.valueOf(userId))
      .signWith(key)
      .issuedAt(new Date())
      .compact();

    return new SessionResponse(jws);
  }

  @PostMapping("/auth/signup")
  public void signup(@RequestBody Signup signup) {
    authService.signup(signup);
  }
}