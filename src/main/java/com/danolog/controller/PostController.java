package com.danolog.controller;

import com.danolog.request.PostCreate;
import com.danolog.response.PostResponse;
import com.danolog.service.PostService;
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
  public void post(@RequestBody @Valid PostCreate request) {

    postService.write(request);
  }

  @GetMapping("/posts/{postId}")
  public PostResponse get(@PathVariable(name = "postId") Long id) {
    return postService.get(id);
  }

  @GetMapping("/posts")
  public List<PostResponse> getList() {
    return postService.getList();
  }
}
