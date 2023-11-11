package com.danolog.api.controller;

import com.danolog.api.repository.UserRepository;
import com.danolog.api.request.Signup;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void clean() {
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("회원가입")
  void test6() throws Exception {
    // given
    Signup signup = Signup.builder()
      .name("danolman")
      .email("danolman@gmail.com")
      .password("1234")
      .build();

    //expected
    mockMvc.perform(post("/auth/signup")
        .content(objectMapper.writeValueAsString(signup))
        .contentType(APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(print());
  }
}