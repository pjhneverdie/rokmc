package com.pjh.jpadrill.project.entity;

import java.util.ArrayList;
import java.util.List;

import com.pjh.jpadrill.common.entity.BaseEntity;
import com.pjh.jpadrill.document.enitity.Document;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = true, length = 255)
    private String description;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    // 아 제약 깜빡함.. 날짜 당연 더 늦게 끝나야지.
    @Embedded
    private ProjectPeriod period;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    protected Project(String name, String description, ProjectStatus status, ProjectPeriod period) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.period = period;
    }

    public static Project createProject(String name, String description, ProjectStatus status, ProjectPeriod period) {
        return Project.builder()
                .name(name)
                .description(description)
                .status(status)
                .period(period)
                .build();
    }

}
