package com.danolog.service;

import com.danolog.domain.Post;
import com.danolog.repository.PostRepository;
import com.danolog.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {


  private final PostRepository postRepository;

  public void write(PostCreate postCreate) {
    Post post = Post.builder()
        .title(postCreate.getTitle())
        .content(postCreate.getContent())
        .build();

    postRepository.save(post);
  }

  public Post get(Long id) {
    Post post = postRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

    return post;
  }
}
