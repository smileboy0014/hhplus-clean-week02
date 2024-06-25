package com.hhplus.clean.lecture.domain.service.dto;

import com.hhplus.clean.lecture.domain.entity.Lecture;
import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record LectureResponse(Long lectureId, String name,
                              Integer totalQuantity, int appliedQuantity,
                              List<LectureHistory> lectureHistories, LocalDateTime dateAppliedStart) {

    public static LectureResponse of(Lecture lecture) {
        return LectureResponse.builder()
                .lectureId(lecture.getLectureId())
                .name(lecture.getName())
                .totalQuantity(lecture.getTotalQuantity())
                .appliedQuantity(lecture.getAppliedQuantity())
                .lectureHistories(lecture.getLectureHistories())
                .dateAppliedStart(lecture.getDateAppliedStart())
                .build();
    }



}
