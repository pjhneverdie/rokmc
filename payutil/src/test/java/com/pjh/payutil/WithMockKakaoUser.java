package com.pjh.payutil;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockKakaoUserSecurityContextFactory.class)
public @interface WithMockKakaoUser {
    String nickname() default "테스트 닉네임";

    String profileImageUrl() default "https://example.com/profile.png";

    long id() default 12345L;
}
