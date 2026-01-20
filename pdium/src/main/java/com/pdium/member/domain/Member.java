package com.pdium.member.domain;

import com.pdium.common.domain.BaseEntity;
import com.pdium.member.enum_type.MemberRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Entity
@Getter
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true, length = 30) // + NOT BLANK + REGEX
    private String email;

    @Column(nullable = false, unique = true, length = 30) // + NOT BLANK
    private String nickname;

    @Column(nullable = false, length = 30) // + NOT BLANK
    private String password;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

}
