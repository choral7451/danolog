package com.danolog.api.controller;

import com.danolog.api.config.DanologMockUser;
import com.danolog.api.domain.Post;
import com.danolog.api.repository.PostRepository;
import com.danolog.api.repository.UserRepository;
import com.danolog.api.request.PostCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.danolog.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @AfterEach
  void clean() {
    postRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("글 단건 조회")
  void test1() throws Exception {
    Post post = Post.builder()
      .title("제목")
      .content("내용")
      .build();

    postRepository.save(post);

    this.mockMvc.perform(get("/posts/{postId}",post.getId())
        .contentType(APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(document("post-inquiry",
        pathParameters(
          parameterWithName("postId").description("게시글 ID")
        ),
        responseFields(
          fieldWithPath("id").description("게시글 ID"),
          fieldWithPath("title").description("게시글 제목"),
          fieldWithPath("content").description("게시글 내용")
        )
      ));
  }

  @Test
  @DanologMockUser
//  @WithMockUser(username = "danolman@gmail.com",roles = {"ADMIN"})
  @DisplayName("글 등록")
  void test2() throws Exception {
    PostCreate request = PostCreate.builder()
      .title("나는 다니엘입니다.")
      .content("내용입니다.")
      .build();

    String json = objectMapper.writeValueAsString(request);

    mockMvc.perform(post("/posts")
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
        .content(json))
      .andExpect(status().isOk())
      .andDo(document("post-create",
        requestFields(
          fieldWithPath("title").description("제목")
            .attributes(key("constraint").value("좋은제목 입력해주세요.")),
          fieldWithPath("content").description("내용").optional()
        )
      ));
  }
}
