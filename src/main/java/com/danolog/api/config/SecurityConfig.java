package com.danolog.api.config;


import com.danolog.api.config.handler.Http401Handler;
import com.danolog.api.config.handler.Http403Handler;
import com.danolog.api.config.handler.LoginFailHandler;
import com.danolog.api.domain.User;
import com.danolog.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final ObjectMapper objectMapper;

  // security ignore
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers("/favicon.ico", "/error");
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .authorizeHttpRequests(request -> request
        .requestMatchers("/auth/signup").permitAll()
        .requestMatchers("/auth/login").permitAll()
        .requestMatchers("/user").hasRole("USER")
        .requestMatchers("/admin").hasRole("ADMIN")
        .anyRequest().authenticated()
      )
      .formLogin(form -> form
        .loginPage("/auth/login")
        .loginProcessingUrl("/auth/login")
        .usernameParameter("username")
        .passwordParameter("password")
        .defaultSuccessUrl("/")
        .failureHandler(new LoginFailHandler(objectMapper))
      )
      .exceptionHandling(e -> {
        e.accessDeniedHandler(new Http403Handler(objectMapper));
        e.authenticationEntryPoint(new Http401Handler(objectMapper));
      })
      .rememberMe(rm  -> rm.rememberMeParameter("remember")
        .alwaysRemember(false)
        .tokenValiditySeconds(2592000)
      )
      .csrf(AbstractHttpConfigurer::disable) // 확인 필요
      .build();
  }

  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepository) {
    return username -> {
      User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));

      return new UserPrincipal(user);
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new SCryptPasswordEncoder(16, 8, 1, 32, 64);
  }
}
