package com.hhplus.clean.lecture.domain.service.dto;

import com.hhplus.clean.lecture.domain.entity.Lecture;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LectureCreateServiceRequest(String name,
                                          Integer totalQuantity,
                                          LocalDateTime dateAppliedStart) {

    public Lecture toEntity() {
        return Lecture.builder()
                .name(name)
                .totalQuantity(totalQuantity)
                .dateAppliedStart(dateAppliedStart)
                .build();
    }
}
