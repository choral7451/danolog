package com.danolog.api.service;

import com.danolog.api.crypto.PasswordEncoder;
import com.danolog.api.domain.User;
import com.danolog.api.exception.AlreadyExistsEmailException;
import com.danolog.api.repository.UserRepository;
import com.danolog.api.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void signup(Signup signup) {
    Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
    if (userOptional.isPresent()) {
      throw new AlreadyExistsEmailException();
    }

    String encryptedPassword = passwordEncoder.encrypt(signup.getPassword());

    var user = User.builder()
      .name(signup.getName())
      .password(encryptedPassword)
      .email(signup.getEmail())
      .build();

    userRepository.save(user);
  }
}
