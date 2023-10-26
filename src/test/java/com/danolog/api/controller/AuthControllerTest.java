package com.danolog.api.controller;

import com.danolog.api.domain.User;
import com.danolog.api.repository.SessionRepository;
import com.danolog.api.repository.UserRepository;
import com.danolog.api.request.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

  @Autowired
  private SessionRepository sessionRepository;

  @BeforeEach
  void clean() {
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("로그인 성공")
  void test() throws Exception {
    // given
    userRepository.save(User.builder()
      .name("daniel")
      .email("artinfokorea2022@gmail.com")
      .password("a123456!")
      .build());

    Login login = Login.builder()
      .email("artinfokorea2022@gmail.com")
      .password("a123456!")
      .build();

    String json = objectMapper.writeValueAsString(login);

    //expected
    mockMvc.perform(post("/auth/login")
        .contentType(APPLICATION_JSON)
        .content(json)
      )
      .andExpect(status().isOk())
      .andDo(print()); // 요청 요약

  }

  @Test
  @Transactional
  @DisplayName("로그인 성공 후 세션 1개 생성")
  void test1() throws Exception {
    // given
    User user = userRepository.save(User.builder()
      .name("daniel")
      .email("artinfokorea2022@gmail.com")
      .password("a123456!")
      .build());

    Login login = Login.builder()
      .email("artinfokorea2022@gmail.com")
      .password("a123456!")
      .build();

    String json = objectMapper.writeValueAsString(login);

    //expected
    mockMvc.perform(post("/auth/login")
        .contentType(APPLICATION_JSON)
        .content(json)
      )
      .andExpect(status().isOk())
      .andDo(print());

    assertEquals(1L, user.getSessions().size());
  }

  @Test
  @DisplayName("로그인 성공 후 세션 세션 응답")
  void test2() throws Exception {
    // given
    User user = userRepository.save(User.builder()
      .name("daniel")
      .email("artinfokorea2022@gmail.com")
      .password("a123456!")
      .build());

    Login login = Login.builder()
      .email("artinfokorea2022@gmail.com")
      .password("a123456!")
      .build();

    String json = objectMapper.writeValueAsString(login);

    //expected
    mockMvc.perform(post("/auth/login")
        .contentType(APPLICATION_JSON)
        .content(json)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()))
      .andDo(print());
  }
}