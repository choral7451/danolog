package com.danolog.controller;

import com.danolog.domain.Post;
import com.danolog.repository.PostRepository;
import com.danolog.request.PostCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PostRepository postRepository;

  @BeforeEach
  void clean() {
    postRepository.deleteAll();
  }

  @Test
  @DisplayName("/posts 요청시 Hello World를 출력한다.")
  void test() throws Exception {
    // given
    PostCreate request = PostCreate.builder()
      .title("제목입니다.")
      .content("내용입니다.")
      .build();

    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(post("/posts")
        .contentType(APPLICATION_JSON)
        .content(json)
      )
      .andExpect(status().isOk())
      .andExpect(content().string(""))
      .andDo(print()); // 요청 요약

  }

  @Test
  @DisplayName("/posts 요청시 title값은 필수다.")
  void test2() throws Exception {
    // given
    PostCreate request = PostCreate.builder()
      .content("내용입니다.")
      .build();

    String json = objectMapper.writeValueAsString(request);

    //expected
    mockMvc.perform(post("/posts")
        .contentType(APPLICATION_JSON)
        .content(json)
      )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.code").value("400"))
      .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
      .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
      .andDo(print()); // 요청 요약

  }

  @Test
  @DisplayName("/posts 요청시 DB에 값이 저장")
  void test3() throws Exception {
    // given
    PostCreate request = PostCreate.builder()
      .title("제목입니다.")
      .content("내용입니다.")
      .build();

    String json = objectMapper.writeValueAsString(request);

    // when
    mockMvc.perform(post("/posts")
        .contentType(APPLICATION_JSON)
        .content(json)
      )
      .andExpect(status().isOk())
      .andDo(print()); // 요청 요약

    // then
    assertEquals(1, postRepository.count());

    Post post = postRepository.findAll().get(0);
    assertEquals("제목입니다.", post.getTitle());
    assertEquals("내용입니다.", post.getContent());
  }

  @Test
  @DisplayName("글 1개 조회")
  void test4() throws Exception {
    // given
    Post post = Post.builder()
      .title("foo")
      .content("bar")
      .build();

    postRepository.save(post);

    // expected
    mockMvc.perform(get("/posts/{postId}", post.getId())
        .contentType(APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(post.getId()))
      .andExpect(jsonPath("$.title").value("foo"))
      .andExpect(jsonPath("$.content").value("bar"))
      .andDo(print()); // 요청 요약
  }
}