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

    @Column(nullable = false, length = 30) // + NOT BLANK
    private String title; 

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DocumentStatus documentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private Document(String title, String content, DocumentStatus documentStatus, Project project) {
        this.title = title;
        this.content = content;
        this.documentStatus = documentStatus;
        this.project = project;
    }

    public static Document createDocument(String title, String content, DocumentStatus documentStatus,
            Project project) {
        return new Document(title, content, documentStatus, project);
    }

}
