package com.danolog.api.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = DanologMockSecurityContext.class)
public @interface DanologMockUser {

  String email() default "danolman91@gmail.com";

  String name() default "다놀맨";

  String password() default "1234";

//  String role() default "ROLE_ADMIN";
}
