package com.pjh.jpadrill.issue.entity;

import com.pjh.jpadrill.common.entity.BaseEntity;
import com.pjh.jpadrill.issue.enumtype.IssueStatus;
import com.pjh.jpadrill.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(nullable = false, length = 30) // + NOT BLANK
    private String title;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
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
