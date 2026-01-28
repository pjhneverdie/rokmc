package com.pdium.security.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public abstract class SecurityUtils {

    private SecurityUtils() {
    }

    public static String getStringAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    public static List<SimpleGrantedAuthority> convertToAuthorities(String authoritiesStr) {
        return Arrays.stream(authoritiesStr.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}