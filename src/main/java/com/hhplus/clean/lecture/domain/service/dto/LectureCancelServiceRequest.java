package com.hhplus.clean.lecture.domain.service.dto;

import lombok.Builder;

@Builder
public record LectureCancelServiceRequest(Long lectureId,
                                          Long userId) {

}
