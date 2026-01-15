package com.pjh.jpadrill.project.entity;

import com.pjh.jpadrill.member.entity.Member;
import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;

import com.pjh.jpadrill.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 30)
    private String role; // + NOT BLANK

    @CreatedDate
    @Column(nullable = false)
    private LocalDate joinedAt;

    private ProjectMember(Project project, Member member, String role) {
        this.project = project;
        this.member = member;
        this.role = role;
    }

    public static ProjectMember createProjectMember(Project project, Member member, String role) {
        return new ProjectMember(project, member, role);
    }

}
