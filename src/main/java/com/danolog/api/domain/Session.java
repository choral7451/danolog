package com.danolog.api.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;
import static java.util.UUID.randomUUID;

@Getter
@Entity
@Table(name = "sessions")
@NoArgsConstructor
public class Session {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String accessToken;

  @ManyToOne
  private User user;

  @Builder
  public Session(User user) {
    this.accessToken = randomUUID().toString();
    this.user = user;
  }
}
