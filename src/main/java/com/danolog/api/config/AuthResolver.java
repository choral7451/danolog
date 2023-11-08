package com.danolog.api.config;

import com.danolog.api.config.data.UserSession;
import com.danolog.api.exception.Unauthorized;
import com.danolog.api.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

  private final SessionRepository sessionRepository;
  private final String KEY = "1oaAWvrgDcYmUZNqnyhJRQHDfWGkVK3Vnt4J0mn7vy0=";

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(UserSession.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    String jws = webRequest.getHeader("Authorization");
    if (jws == null || jws.isEmpty()) {
      throw new Unauthorized();
    }

    SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));

    try {
      Jws<Claims> claims = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(jws);

      String userId = (String) claims.getPayload().getSubject();

      return new UserSession(Long.parseLong(userId));
    } catch (JwtException e) {
      throw new Unauthorized();
    }
  }
}
