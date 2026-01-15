package com.pjh.jpadrill.member.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true, length = 30) // + NOT BLANK + REGEX
    private String email;

    @Column(nullable = false, length = 30) // + NOT BLANK 
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MemberRole role;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    private Member(String email, String name, MemberRole role, Address address) {
        Assert.notNull(email, "email must not be null");
        Assert.hasText(email, "email must not be blank");
        Assert.isTrue(email.length() <= 30, "email length must be <= 30");
        Assert.isTrue(email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$"), "email format is invalid");

        Assert.notNull(name, "name must not be null");
        Assert.hasText(name, "name must not be blank");
        Assert.isTrue(name.length() <= 30, "name length must be <= 30");

        Assert.notNull(role, "role must not be null");
        Assert.isTrue(role.name().length() <= 30, "role length must be <= 30");

        Assert.notNull(address, "address must not be null");

        this.email = email;
        this.name = name;
        this.role = role;
        this.address = address;
    }

    public static Member createMember(String email, String name, MemberRole role, Address address) {
        return new Member(email, name, role, address);
    }

}
