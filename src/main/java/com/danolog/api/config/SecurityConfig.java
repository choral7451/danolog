package com.danolog.api.config;


import com.danolog.api.config.filter.EmailPasswordAuthFilter;
import com.danolog.api.config.handler.Http401Handler;
import com.danolog.api.config.handler.Http403Handler;
import com.danolog.api.config.handler.LoginFailHandler;
import com.danolog.api.domain.User;
import com.danolog.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final ObjectMapper objectMapper;
  private final UserRepository userRepository;

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
      .addFilterBefore(emailPasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
//      .formLogin(form -> form
//        .loginPage("/auth/login")
//        .loginProcessingUrl("/auth/login")
//        .usernameParameter("username")
//        .passwordParameter("password")
//        .defaultSuccessUrl("/")
//        .failureHandler(new LoginFailHandler(objectMapper))
//      )
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
  public EmailPasswordAuthFilter emailPasswordAuthFilter() {
    EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/auth/login",objectMapper);
    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
    filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
    filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

    SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
    rememberMeServices.setAlwaysRemember(true);
    rememberMeServices.setValiditySeconds(3600 * 24 * 30);
    filter.setRememberMeServices(rememberMeServices);
    return filter;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService(userRepository));
    provider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(provider);
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
