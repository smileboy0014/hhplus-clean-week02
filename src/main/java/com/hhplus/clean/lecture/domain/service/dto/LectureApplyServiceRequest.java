package com.hhplus.clean.lecture.domain.service.dto;

import com.hhplus.clean.lecture.domain.entity.Lecture;
import com.hhplus.clean.lecture.domain.entity.LectureHistory;

public record LectureApplyServiceRequest(Long lectureId, Long userId) {

    public LectureHistory toEntity(Lecture lecture) {
        return LectureHistory.builder()
                .lecture(lecture)
                .userId(userId)
                .build();

    }
}
