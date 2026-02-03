package com.pdium.member.enum_type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    PJH("ROLE_PJH"), GUEST("ROLE_GUEST");

    private final String value;
}
