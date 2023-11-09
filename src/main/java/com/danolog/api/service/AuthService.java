package com.danolog.api.service;

import com.danolog.api.domain.Session;
import com.danolog.api.domain.User;
import com.danolog.api.exception.AlreadyExistsEmailException;
import com.danolog.api.exception.InvalidRequest;
import com.danolog.api.exception.InvalidSigninInformation;
import com.danolog.api.repository.UserRepository;
import com.danolog.api.request.Login;
import com.danolog.api.request.Signup;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  @Transactional
  public Long signin(Login login) {
    User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
      .orElseThrow(InvalidSigninInformation::new);
    Session session = user.addSession();

    return user.getId();
  }

  public void signup(Signup signup) {
    Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
    if (userOptional.isPresent()) {
      throw new AlreadyExistsEmailException();
    }

    var user = User.builder()
      .name(signup.getName())
      .password(signup.getPassword())
      .email(signup.getEmail())
      .build();

    userRepository.save(user);
  }
}
