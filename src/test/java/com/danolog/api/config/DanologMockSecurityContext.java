package com.danolog.api.config;

import com.danolog.api.domain.User;
import com.danolog.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class DanologMockSecurityContext implements WithSecurityContextFactory<DanologMockUser> {

  private final UserRepository userRepository;

  @Override
  public SecurityContext createSecurityContext(DanologMockUser annotation) {
    User user = User.builder()
      .email(annotation.email())
      .password(annotation.password())
      .name(annotation.name())
      .build();

    userRepository.save(user);

    var principal = new UserPrincipal(user);

    var role = new SimpleGrantedAuthority("ROLE_ADMIN");
    var authenticationToken = new UsernamePasswordAuthenticationToken(principal, user.getPassword(), List.of(role));

    var context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authenticationToken);

    return context;
  }
}
