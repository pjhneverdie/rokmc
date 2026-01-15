package com.pjh.jpadrill.project.entity;

import java.util.ArrayList;
import java.util.List;

import com.pjh.jpadrill.common.entity.BaseEntity;
import com.pjh.jpadrill.document.enitity.Document;
import com.pjh.jpadrill.member.entity.Member;
import com.pjh.jpadrill.project.enumtype.ProjectStatus;
import com.pjh.jpadrill.project.vo.ProjectPeriod;

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
public class Project extends BaseEntity {

    @Column(nullable = false, length = 30) // + NOT BLANK
    private String name;

    @Column(nullable = true, length = 255)
    private String description;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Embedded
    private ProjectPeriod period; // + start_date < end_date

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    private Project(String name, String description, ProjectStatus status, ProjectPeriod period) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.period = period;
    }

    public static Project createProject(String name, String description, ProjectStatus status, ProjectPeriod period) {
        return new Project(name, description, status, period);
    }

    public void addMember(Member member, String role) {
        ProjectMember projectMember = ProjectMember.createProjectMember(this, member, role);

        this.projectMembers.add(projectMember);

        // 이렇게 하는 것보다는Member 쪽에 addProjectMember 메서드를 만들어서 양방향 연관관계 설정을 해주는 것이 더 좋음
        member.getProjectMembers().add(projectMember);
    }

}
