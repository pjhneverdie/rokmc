package com.pjh.jpadrill.issue.entity;

import java.time.LocalDateTime;

import com.pjh.jpadrill.issue.enumtype.IssueStatus;
import com.pjh.jpadrill.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskIssue extends Issue {

    @Column(nullable = false)
    private LocalDateTime dueDate;

    private TaskIssue(String title, String description, IssueStatus status, Member assignee, LocalDateTime dueDate) {
        super(title, description, status, assignee);
        this.dueDate = dueDate;
    }

    public static TaskIssue createTaskIssue(String title, String description, Member assignee, LocalDateTime dueDate) {
        return new TaskIssue(title, description, IssueStatus.OPEN, assignee, dueDate);
    }

}
