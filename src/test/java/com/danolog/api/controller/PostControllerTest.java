package com.danolog.api.controller;

import com.danolog.api.domain.Post;
import com.danolog.api.repository.PostRepository;
import com.danolog.api.request.PostCreate;
import com.danolog.api.request.PostEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
  @DisplayName("글 작성 요청시 title값은 필수다.")
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
      .andDo(print()); // 요청 요약

  }

  @Test
  @DisplayName("글 작성 요청시 DB에 값이 저장")
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
      .title("123456789123456")
      .content("bar")
      .build();

    postRepository.save(post);

    // expected
    mockMvc.perform(get("/posts/{postId}", post.getId())
        .contentType(APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(post.getId()))
      .andExpect(jsonPath("$.title").value("1234567891"))
      .andExpect(jsonPath("$.content").value("bar"))
      .andDo(print()); // 요청 요약
  }

  @Test
  @DisplayName("글 여러개 조회")
  void test5() throws Exception {
    // given
    List<Post> requestPosts = IntStream.range(1, 31)
      .mapToObj(i ->
        Post.builder()
          .title("제목 - " + i)
          .content("서초 자이 - " + i)
          .build()
      ).toList();

    postRepository.saveAll(requestPosts);

    // expected
    mockMvc.perform(get("/posts")
        .param("page","1")
        .param("size", "5")
        .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("제목 - 30"))
        .andExpect(jsonPath("$[0].content").value("서초 자이 - 30"))
        .andDo(print());
  }

  @Test
  @DisplayName("페이지를 0으로 요청하면 첫페이지를 가지고 온다.")
  void test6() throws Exception {
    // given
    List<Post> requestPosts = IntStream.range(1, 31)
      .mapToObj(i ->
        Post.builder()
          .title("제목 - " + i)
          .content("서초 자이 - " + i)
          .build()
      ).toList();

    postRepository.saveAll(requestPosts);

    // expected
    mockMvc.perform(get("/posts")
        .param("page","0")
        .param("size", "5")
        .contentType(APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].title").value("제목 - 30"))
      .andExpect(jsonPath("$[0].content").value("서초 자이 - 30"))
      .andDo(print());
  }

  @Test
  @DisplayName("글 제목 수정")
  void test7() throws Exception {
    // given
    Post post  = Post.builder()
      .title("다니엘")
      .content("내용")
      .build();
    postRepository.save(post);

    PostEdit postEdit = PostEdit.builder()
      .title("daniel")
      .content("내용")
      .build();

    // when

    // expected
    mockMvc.perform(patch("/posts/{postId}", post.getId())
        .contentType(APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(postEdit))
      )
      .andExpect(status().isOk())
      .andDo(print());
  }

  @Test
  @DisplayName("게시글 삭제")
  void test8() throws Exception {
    // given
    Post post  = Post.builder()
      .title("다니엘")
      .content("내용")
      .build();
    postRepository.save(post);

    // expected
    mockMvc.perform(delete("/posts/{postId}", post.getId())
        .contentType(APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(print());
  }

  @Test
  @DisplayName("존재하지 않는 게시글 조회")
  void test9() throws Exception {
    // expected
    mockMvc.perform(get("/posts/{postId}", 1L)
        .contentType(APPLICATION_JSON))
      .andExpect(status().isNotFound())
      .andDo(print());
  }

  @Test
  @DisplayName("존재하지 않는 게시글 수정")
  void test10() throws Exception {
    // given
    PostEdit postEdit = PostEdit.builder()
      .title("daniel")
      .content("내용")
      .build();

    // expected
    mockMvc.perform(patch("/posts/{postId}", 1L)
        .contentType(APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(postEdit))
      )
      .andExpect(status().isNotFound())
      .andDo(print());
  }

  @Test
  @DisplayName("게시글 작성시 제목에 '바보'는 포함될 수 없다.")
  void test11() throws Exception {
    // given
    PostCreate request = PostCreate.builder()
      .title("나는 바보입니다.")
      .content("내용입니다.")
      .build();

    String json = objectMapper.writeValueAsString(request);

    // when
    mockMvc.perform(post("/posts")
        .contentType(APPLICATION_JSON)
        .content(json))
      .andExpect(status().isBadRequest())
      .andDo(print()); // 요청 요약
  }

}