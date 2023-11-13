package com.danolog.api.controller;

import com.danolog.api.config.UserPrincipal;
import com.danolog.api.request.PostCreate;
import com.danolog.api.request.PostEdit;
import com.danolog.api.request.PostSearch;
import com.danolog.api.response.PostResponse;
import com.danolog.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/posts")
  public void post(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid PostCreate request) {
    request.validate();
    postService.write(userPrincipal.getUserId(), request);
  }


  @GetMapping("/posts/{postId}")
  public PostResponse get(@PathVariable(name = "postId") Long id) {
    return postService.get(id);
  }


  @GetMapping("/posts")
  public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
    return postService.getList(postSearch);
  }


  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PatchMapping("/posts/{postId}")
  public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit) {
    postService.edit(postId, postEdit);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN') && hasPermission(#postId, 'POST', 'DELETE')")
  @DeleteMapping("/posts/{postId}")
  public void delete(@PathVariable Long postId) {
    postService.delete(postId);
  }
}
