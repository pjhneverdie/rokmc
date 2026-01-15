package com.pjh.jpadrill.project.vo;

import java.time.LocalDate;

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
    private LocalDate startDate; // + start_date < end_date

    @Column(nullable = false)
    private LocalDate endDate; // + start_date < end_date

    public ProjectPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
