package com.pjh.jpadrill.project.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectPeriod {

    @Column(nullable = false)
    private LocalDateTime startDate; // + start_date < end_date

    @Column(nullable = false)
    private LocalDateTime endDate; // + start_date < end_date

    public ProjectPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
