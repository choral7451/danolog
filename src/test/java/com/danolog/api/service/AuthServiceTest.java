package com.danolog.api.service;

import com.danolog.api.domain.User;
import com.danolog.api.exception.AlreadyExistsEmailException;
import com.danolog.api.repository.UserRepository;
import com.danolog.api.request.Signup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class AuthServiceTest {


  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthService authService;

  @BeforeEach
  void clean() {
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("회원가입 성공")
  void test1() {
    // given
    Signup signup = Signup.builder()
      .name("danolman")
      .email("danolman@gmail.com")
      .password("1234")
      .build();

    // when
    authService.signup(signup);

    // then
    assertEquals(1, userRepository.count());

    User user = userRepository.findAll().iterator().next();
    assertEquals("danolman@gmail.com", user.getEmail());
    assertNotNull(user.getPassword());
    assertNotEquals("1234", user.getPassword());
    assertEquals("danolman", user.getName());
  }

  @Test
  @DisplayName("회원가입시 중복된 이메일")
  void test2() {
    // given
    User user = User.builder()
      .name("danolman")
      .email("danolman@gmail.com")
      .password("1234")
      .build();

    userRepository.save(user);

    Signup signup = Signup.builder()
      .name("danolman")
      .email("danolman@gmail.com")
      .password("1234")
      .build();

    // when
    assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));

  }
}