package com.danolog.api.repository;

import com.danolog.api.domain.Post;
import com.danolog.api.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.danolog.api.domain.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Post> getList(PostSearch postSearch) {
    return jpaQueryFactory.selectFrom(post)
      .limit(postSearch.getSize())
      .offset(postSearch.getOffset())
      .orderBy(post.id.desc())
      .fetch();
  }
}
