package com.pjh.jpadrill.issue.entity;

import java.lang.reflect.Member;

import com.pjh.jpadrill.issue.enumtype.IssueStatus;
import com.pjh.jpadrill.issue.enumtype.Severity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorColumn(name = "bug")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BugIssue extends Issue {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Severity severity;

    @Builder(access = AccessLevel.PRIVATE)
    private BugIssue(String title, String description, IssueStatus status, Severity severity, Member assignee) {
        super(title, description, status, assignee);
        this.severity = severity;
    }

    public static BugIssue createBugIssue(String title, String description, Severity severity,
            Member assignee) {
        return BugIssue.builder()
                .title(title)
                .description(description)
                .status(IssueStatus.OPEN)
                .severity(severity)
                .assignee(assignee)
                .build();
    }
    
}
