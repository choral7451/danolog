package com.danolog.api.service;

import com.danolog.api.domain.Session;
import com.danolog.api.domain.User;
import com.danolog.api.exception.InvalidSigninInformation;
import com.danolog.api.repository.UserRepository;
import com.danolog.api.request.Login;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  public String signin(Login login) {
    User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
      .orElseThrow(InvalidSigninInformation::new);
    Session session = user.addSession();

    return session.getAccessToken();
  }
}
