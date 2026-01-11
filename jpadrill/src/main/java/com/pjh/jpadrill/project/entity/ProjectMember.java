package com.pjh.jpadrill.project.entity;

import java.lang.reflect.Member;
import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;

import com.pjh.jpadrill.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "project_member", uniqueConstraints = {
        @UniqueConstraint(name = "prevent_duplicated_project_member", columnNames = { "project_id", "member_id" })
})
public class ProjectMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)

    private Member member;

    @Column(nullable = false, length = 30)
    private String role;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate joinedAt;

    @Builder
    protected ProjectMember(Project project, Member member, String role) {
        this.project = project;
        this.member = member;
        this.role = role;
    }

}
