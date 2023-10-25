package com.danolog.api.controller;

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

  @PostMapping("/posts")
  public void post(@RequestBody @Valid PostCreate request, @RequestHeader String authorization) {
    if (authorization.equals("danolman")) {
      request.validate();
      postService.write(request);
    }
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
  public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit, @RequestHeader String authorization) {
    if (authorization.equals("danolman")) {
      postService.edit(postId, postEdit);
    }
  }

  @DeleteMapping("/posts/{postId}")
  public void delete(@PathVariable Long postId, @RequestHeader String authorization) {
    if (authorization.equals("danolman")) {
      postService.delete(postId);
    }
  }
}
