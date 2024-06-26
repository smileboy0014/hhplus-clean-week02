package com.hhplus.clean.lecture.controller;

import com.hhplus.clean.lecture.controller.dto.ApiResponse;
import com.hhplus.clean.lecture.controller.dto.LectureApplyRequest;
import com.hhplus.clean.lecture.domain.entity.Lecture;
import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import com.hhplus.clean.lecture.domain.service.LectureService;
import com.hhplus.clean.lecture.domain.service.dto.HistoryResponse;
import com.hhplus.clean.lecture.domain.service.dto.LectureResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    // 특강 목록을 조회
    @GetMapping
    public ApiResponse<List<LectureResponse>> getLectures() {
        return ApiResponse.ok(lectureService.getLectures());
    }

    // 특강 신청
    @PostMapping("/apply")
    public ApiResponse<HistoryResponse> apply(@RequestBody @Valid LectureApplyRequest request) {
        return ApiResponse.ok(lectureService.applyLecture(request.toServiceRequest()));
    }

    // 특강 신청 내역에 유저가 있는지 확인
    @GetMapping("/check/{lectureId}")
    public ApiResponse<Boolean> checkHistories(@PathVariable Long lectureId, @RequestParam Long userId) {
        return ApiResponse.ok(lectureService.checkHistories(lectureId, userId));
    }
}
