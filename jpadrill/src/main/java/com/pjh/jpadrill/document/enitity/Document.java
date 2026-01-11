package com.pjh.jpadrill.document.enitity;

import org.springframework.util.Assert;

import com.pjh.jpadrill.common.entity.BaseEntity;
import com.pjh.jpadrill.document.enumtype.DocumentStatus;
import com.pjh.jpadrill.project.entity.Project;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Document extends BaseEntity {

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DocumentStatus documentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Builder(access = AccessLevel.PRIVATE)
    protected Document(String title, String content, DocumentStatus documentStatus, Project project) {
        Assert.hasText(title, "제목은 필수이며 공백일 수 없습니다.");
        Assert.isTrue(title.length() <= 30, "제목의 길이는 30자를 초과할 수 없습니다.");

        Assert.hasText(content, "내용은 필수이며 공백일 수 없습니다.");

        Assert.notNull(documentStatus, "문서 상태는 필수입니다.");

        Assert.notNull(project, "대상 프로젝트는 필수입니다.");

        this.title = title;
        this.content = content;
        this.documentStatus = documentStatus;
        this.project = project;
    }

    public static Document createDocument(String title, String content, DocumentStatus documentStatus,
            Project project) {
        return Document.builder()
                .title(title)
                .content(content)
                .documentStatus(documentStatus)
                .project(project)
                .build();
    }

}
