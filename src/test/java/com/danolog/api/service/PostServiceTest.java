package com.danolog.api.service;

import com.danolog.api.domain.Post;
import com.danolog.api.exception.PostNotFound;
import com.danolog.api.repository.PostRepository;
import com.danolog.api.request.PostCreate;
import com.danolog.api.request.PostEdit;
import com.danolog.api.request.PostSearch;
import com.danolog.api.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

  @Autowired
  private PostService postService;

  @Autowired
  private PostRepository postRepository;

  @BeforeEach
  void clean() {
    postRepository.deleteAll();
  }

  @Test
  @DisplayName("글 작성")
  void test1() {
    // given

    PostCreate postCreate = PostCreate.builder()
      .title("제목입니다.")
      .content("내용입니다.")
      .build();

    // when
    postService.write(postCreate);

    // then
    assertEquals(1, postRepository.count());
    Post post = postRepository.findAll().get(0);
    assertEquals("제목입니다.", post.getTitle());
    assertEquals("내용입니다.", post.getContent());
  }

  @Test
  @DisplayName("글 1개 조회")
  void test2() {
    // given
    Post requestPost = Post.builder()
      .title("foo")
      .content("bar")
      .build();
    postRepository.save(requestPost);

    // when
    PostResponse response = postService.get(requestPost.getId());

    // then
    assertNotNull(response);
    assertEquals(1L, postRepository.count());
    assertEquals("foo", response.getTitle());
    assertEquals("bar", response.getContent());
  }

  @Test
  @DisplayName("글 1페이지 조회")
  void test3() {
    // given
    List<Post> requestPosts = IntStream.range(1, 31)
      .mapToObj(i ->
        Post.builder()
          .title("제목 - " + i)
          .content("서초 자이 - " + i)
          .build()
      ).toList();

    postRepository.saveAll(requestPosts);

    PostSearch postSearch = PostSearch.builder()
      .page(1)
      .build();

    // when
    List<PostResponse> posts = postService.getList(postSearch) ;

    // then
    assertEquals(20L, posts.size());
    assertEquals("제목 - 30", posts.get(0).getTitle());
  }

  @Test
  @DisplayName("글 제목 수정")
  void test4() {
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
    postService.edit(post.getId(), postEdit);

    // then
    Post changedPost = postRepository.findById(post.getId())
      .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. - " + post.getId()));

    Assertions.assertEquals("daniel", changedPost.getTitle());
    Assertions.assertEquals("내용", changedPost.getContent());
  }

  @Test
  @DisplayName("글 내용 수정")
  void test5() {
    // given
    Post post  = Post.builder()
      .title("다니엘")
      .content("내용")
      .build();
    postRepository.save(post);

    PostEdit postEdit = PostEdit.builder()
      .title("다니엘")
      .content(null)
      .build();

    // when
    postService.edit(post.getId(), postEdit);

    // then
    Post changedPost = postRepository.findById(post.getId())
      .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. - " + post.getId()));

    Assertions.assertEquals("다니엘", changedPost.getTitle());
    Assertions.assertEquals("내용", changedPost.getContent());
  }

  @Test
  @DisplayName("게시글 삭제")
  void test6() {
    // given
    Post post  = Post.builder()
      .title("다니엘")
      .content("내용")
      .build();
    postRepository.save(post);

    // when
    postService.delete(post.getId());

    // then
    assertEquals(0, postRepository.count());
  }

  @Test
  @DisplayName("글 1개 조회")
  void test7() {
    // given
    Post post = Post.builder()
      .title("다니엘")
      .content("서초구")
      .build();
    postRepository.save(post);

    // expected
    assertThrows(PostNotFound.class, () -> {
      postService.get(post.getId() + 1L);
    });
  }

  @Test
  @DisplayName("게시글 삭제 - 존재하지 않는 글")
  void test8() {
    // given
    Post post  = Post.builder()
      .title("다니엘")
      .content("내용")
      .build();
    postRepository.save(post);

    // then
    assertThrows(PostNotFound.class, () -> {
      postService.delete(post.getId() + 1L);
    });
  }

  @Test
  @DisplayName("글 내용 수정 - 존재하지 않는 글")
  void test9() {
    // given
    Post post  = Post.builder()
      .title("다니엘")
      .content("내용")
      .build();
    postRepository.save(post);

    PostEdit postEdit = PostEdit.builder()
      .title("다니엘")
      .content(null)
      .build();

    // expected
    assertThrows(PostNotFound.class, () -> {
      postService.edit(post.getId() + 1L, postEdit);
    });
  }
}