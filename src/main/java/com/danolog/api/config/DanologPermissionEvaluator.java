package com.danolog.api.config;

import com.danolog.api.exception.PostNotFound;
import com.danolog.api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@RequiredArgsConstructor
public class DanologPermissionEvaluator implements PermissionEvaluator {

  private final PostRepository postRepository;

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
    return false;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
    var userPrincipal = (UserPrincipal) authentication.getPrincipal();

    var post = postRepository.findById((Long) targetId)
      .orElseThrow(PostNotFound::new);

    return post.getUserId().equals(userPrincipal.getUserId());
  }
}
