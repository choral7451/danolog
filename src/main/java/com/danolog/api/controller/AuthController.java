package com.danolog.api.controller;

import com.danolog.api.domain.User;
import com.danolog.api.exception.InvalidSigninInformation;
import com.danolog.api.repository.UserRepository;
import com.danolog.api.request.Login;
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

  private final UserRepository userRepository;

  @PostMapping("/auth/login")
  public User login(@RequestBody @Valid Login login) {
    log.info(">>> {}", login);

    User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
      .orElseThrow(InvalidSigninInformation::new);

    log.info(">>> {}", user);
    return user;
  }
}
