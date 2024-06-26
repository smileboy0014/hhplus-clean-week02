package com.hhplus.clean.lecture.domain.service.dto;

import lombok.Builder;

@Builder
public record HistoryResponse(Long historyId, Long lectureId, Long userId) {

    public static HistoryResponse of(LectureApplyServiceRequest request) {
        return HistoryResponse.builder()
                .lectureId(request.lectureId())
                .userId(request.userId())
                .build();
    }


}
