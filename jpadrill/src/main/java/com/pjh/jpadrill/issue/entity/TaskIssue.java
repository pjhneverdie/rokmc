package com.pjh.jpadrill.issue.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class TaskIssue extends Issue {

    @Column(nullable = false)
    private LocalDateTime dueDate;

}
