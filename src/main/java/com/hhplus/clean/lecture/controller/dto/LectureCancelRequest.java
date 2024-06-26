package com.hhplus.clean.lecture.controller.dto;

import com.hhplus.clean.lecture.domain.service.dto.LectureCancelServiceRequest;
import jakarta.validation.constraints.NotNull;

public record LectureCancelRequest(@NotNull Long lectureId, @NotNull Long userId) {
    public LectureCancelServiceRequest toServiceRequest() {

        return LectureCancelServiceRequest.builder()
                .lectureId(lectureId)
                .userId(userId)
                .build();
    }
}
