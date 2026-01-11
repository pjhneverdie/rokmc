package com.pjh.jpadrill.member.entity;

import java.util.ArrayList;
import java.util.List;

import com.pjh.jpadrill.common.entity.BaseEntity;
import com.pjh.jpadrill.member.enumtype.MemberRole;
import com.pjh.jpadrill.member.vo.Address;
import com.pjh.jpadrill.project.entity.ProjectMember;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true, length = 30)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MemberRole role;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @Builder
    protected Member(String email, String name, MemberRole role, Address address) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.address = address;
    }

}
