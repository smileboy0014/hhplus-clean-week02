package com.hhplus.clean.lecture.domain.service.dto;

import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import lombok.Builder;

@Builder
public record HistoryResponse(Long historyId, Long lectureId, Long userId) {

    public static HistoryResponse of(LectureHistory history) {
        return HistoryResponse.builder()
                .historyId(history.getHistoryId())
                .lectureId(history.getLecture().getLectureId())
                .userId(history.getUserId())
                .build();
    }
}
