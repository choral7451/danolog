package com.danolog.api.config;


import com.danolog.api.domain.User;
import com.danolog.api.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
public class SecurityConfig {


  // security ignore
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers("/favicon.ico", "/error");
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .authorizeHttpRequests(request -> request
        .requestMatchers(HttpMethod.POST,"/auth/signup").permitAll()
        .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
        .anyRequest().authenticated()
      )
      .formLogin(form -> form
        .loginPage("/auth/login")
        .loginProcessingUrl("/auth/login")
        .usernameParameter("username")
        .passwordParameter("password")
        .defaultSuccessUrl("/")
      )
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
