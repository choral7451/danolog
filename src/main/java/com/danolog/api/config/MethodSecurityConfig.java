package com.danolog.api.config;

import com.danolog.api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class MethodSecurityConfig {

  private final PostRepository postRepository;

  @Bean
  public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
    var handler = new DefaultMethodSecurityExpressionHandler();
    handler.setPermissionEvaluator(new DanologPermissionEvaluator(postRepository));
    return handler;
  }
}
