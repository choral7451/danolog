package com.danolog.api.controller;

import com.danolog.api.config.data.UserSession;
import com.danolog.api.request.PostCreate;
import com.danolog.api.request.PostEdit;
import com.danolog.api.request.PostSearch;
import com.danolog.api.response.PostResponse;
import com.danolog.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @GetMapping("/foo")
  public Long foo(UserSession userSession) {
    log.info(">>> {}", userSession.id);
    return userSession.id;
  }

  @GetMapping("/bar")
  public String bar() {
    return "인즈이 필요없는 페이지";
  }

  @PostMapping("/posts")
  public void post(@RequestBody @Valid PostCreate request) {
    request.validate();
    postService.write(request);
  }

  @GetMapping("/posts/{postId}")
  public PostResponse get(@PathVariable(name = "postId") Long id) {
    return postService.get(id);
  }

  @GetMapping("/posts")
  public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
    return postService.getList(postSearch);
  }


  @PatchMapping("/posts/{postId}")
  public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit) {
    postService.edit(postId, postEdit);
  }

  @DeleteMapping("/posts/{postId}")
  public void delete(@PathVariable Long postId) {
    postService.delete(postId);
  }
}
