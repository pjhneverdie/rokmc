package com.pdium.jwt.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class JwtContants {

    public static final String ROLES_KEY = "roles";
    public static final String NICKNAME_KEY = "nickname";
    public static final String TYPE_DISCRIMINATOR_KEY = "token_type";

    public static final String REFRESH_TOKEN_PREFIX = "rt";
    public static final String BLACKLIST_PREFIX = "black_";
    public static final String BLACKLIST_REASON = "logout";

    @Getter
    @RequiredArgsConstructor
    public enum TokenType {
        ACCESS("access"),
        REFRESH("refresh");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum BlackReason {
        LOGOUT("logout");

        private final String value;
    }

    private JwtContants() {
    }

}
