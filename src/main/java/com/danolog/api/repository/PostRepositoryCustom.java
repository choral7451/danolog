package com.danolog.api.repository;

import com.danolog.api.domain.Post;
import com.danolog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

  List<Post> getList(PostSearch postSearch);
}
