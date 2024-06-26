package com.hhplus.clean.lecture.controller.dto;

import com.hhplus.clean.lecture.domain.service.dto.LectureCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LectureCreateRequest(@NotBlank(message = "강의명 은 필수값 입니다.") String name,
                                   @NotNull(message = "총 정원 수는 필수값 입니다.") Integer totalQuantity,
                                   LocalDateTime dateAppliedStart) {
    public LectureCreateServiceRequest toServiceRequest() {
        return LectureCreateServiceRequest.builder()
                .name(name)
                .totalQuantity(totalQuantity)
                .dateAppliedStart(dateAppliedStart)
                .build();
    }
}
