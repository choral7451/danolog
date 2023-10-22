package com.danolog.api.service;

import com.danolog.api.domain.Post;
import com.danolog.api.domain.PostEditor;
import com.danolog.api.repository.PostRepository;
import com.danolog.api.request.PostCreate;
import com.danolog.api.request.PostEdit;
import com.danolog.api.request.PostSearch;
import com.danolog.api.response.PostResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
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

  public PostResponse get(Long id) {
    Post post = postRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

    return PostResponse.builder()
      .id(post.getId())
      .title(post.getTitle())
      .content(post.getContent())
      .build();
  }

  public List<PostResponse> getList(PostSearch postSearch) {
    return postRepository.getList(postSearch).stream()
      .map(PostResponse::new)
      .collect(Collectors.toList());
  }

  public void edit(Long id, PostEdit postEdit) {
    Post post = postRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

    PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();

    PostEditor postEditor = postEditorBuilder.title(postEdit.getTitle())
      .content(postEdit.getContent())
      .build();

    post.edit(postEditor);
  }

  public void delete(Long id) {
    Post post = postRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

    postRepository.delete(post);
  }
}
