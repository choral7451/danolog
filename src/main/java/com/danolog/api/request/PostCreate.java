package com.danolog.api.request;

import com.danolog.api.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString  // ?
public class PostCreate {

  @NotBlank(message = "타이틀을 입력해주세요.")
  private final String title;

  @NotBlank(message = "컨텐츠을 입력해주세요.")
  private final String content;

  @Builder
  public PostCreate(String title, String content) {
    this.title  = title;
    this.content = content;
  }

  public PostCreate changeTitle(String title) {
    return PostCreate.builder()
      .title(title)
      .content(content)
      .build();
  }

  public void validate() {
    if (title.contains("바보")) {
      throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
    }
  }
}
