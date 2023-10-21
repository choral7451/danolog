package com.danolog.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @Lob // DB에서 long text 형태
  private String content;

  @Builder
  public Post(String title, String content) {
    this.title = title;
    this.content = content;
  }

  public PostEditor.PostEditorBuilder toEditor() {
    return PostEditor.builder()
      .title(title)
      .content(content);
  }

  public void edit(PostEditor postEditor) {
    title = postEditor.getTitle();
    content = postEditor.getContent();
  }
}
