package com.pjh.jpadrill.issue.entity;

import com.pjh.jpadrill.member.entity.Member;
import com.pjh.jpadrill.issue.enumtype.IssueStatus;
import com.pjh.jpadrill.issue.enumtype.Severity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("bug")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BugIssue extends Issue {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Severity severity;

    private BugIssue(String title, String description, IssueStatus status, Severity severity, Member assignee) {
        super(title, description, status, assignee);
        this.severity = severity;
    }

    public static BugIssue createBugIssue(String title, String description, Severity severity,
            Member assignee) {
        return new BugIssue(title, description, IssueStatus.OPEN, severity, assignee);
    }

}
