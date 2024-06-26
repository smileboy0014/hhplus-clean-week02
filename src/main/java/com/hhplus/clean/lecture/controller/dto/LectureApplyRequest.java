package com.hhplus.clean.lecture.controller.dto;

import com.hhplus.clean.lecture.domain.service.dto.LectureApplyServiceRequest;
import jakarta.validation.constraints.NotNull;

public record LectureApplyRequest(@NotNull(message = "강의 ID는 필수값 입니다.") Long lectureId,
                                  @NotNull(message = "유저 ID는 필수값 입니다.")Long userId) {

    public LectureApplyServiceRequest toServiceRequest() {
        return new LectureApplyServiceRequest(lectureId, userId);
    }
}

