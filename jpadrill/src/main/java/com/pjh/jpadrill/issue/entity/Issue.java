package com.pjh.jpadrill.issue.entity;

import java.lang.reflect.Member;

import com.pjh.jpadrill.common.entity.BaseEntity;
import com.pjh.jpadrill.issue.enumtype.IssueStatus;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "issue_type")
@NoArgsConstructor
public abstract class Issue extends BaseEntity {

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 30)
    private IssueStatus status;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member assignee;

    protected Issue(String title, String description, IssueStatus status, Member assignee) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignee = assignee;
    }

}
