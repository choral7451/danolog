package com.danolog.api.service;

import com.danolog.api.crypto.PasswordEncoder;
import com.danolog.api.domain.User;
import com.danolog.api.exception.AlreadyExistsEmailException;
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
    User user = userRepository.findByEmail(login.getEmail())
      .orElseThrow(InvalidSigninInformation::new);

    PasswordEncoder encoder = new PasswordEncoder();

    var matches = encoder.matches(login.getPassword(), user.getPassword());
    if (!matches) {
      throw new InvalidSigninInformation();
    }

    return user.getId();
  }

  public void signup(Signup signup) {
    Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
    if (userOptional.isPresent()) {
      throw new AlreadyExistsEmailException();
    }

    PasswordEncoder encoder = new PasswordEncoder();

    String encryptedPassword = encoder.encrypt(signup.getPassword());

    var user = User.builder()
      .name(signup.getName())
      .password(encryptedPassword)
      .email(signup.getEmail())
      .build();

    userRepository.save(user);
  }
}
