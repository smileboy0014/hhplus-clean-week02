package com.hhplus.clean.lecture.controller;

import com.hhplus.clean.lecture.controller.dto.ApiResponse;
import com.hhplus.clean.lecture.controller.dto.LectureApplyRequest;
import com.hhplus.clean.lecture.controller.dto.LectureCancelRequest;
import com.hhplus.clean.lecture.controller.dto.LectureCreateRequest;
import com.hhplus.clean.lecture.domain.service.LectureService;
import com.hhplus.clean.lecture.domain.service.dto.HistoryResponse;
import com.hhplus.clean.lecture.domain.service.dto.LectureResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hhplus.clean.lecture.controller.enums.LectureEnum.SUCCESS_CANCEL_LECTURE;
import static com.hhplus.clean.lecture.controller.enums.LectureEnum.SUCCESS_DELETE_LECTURE;

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

    // 특강 등록
    @PostMapping
    public ApiResponse<LectureResponse> createLecture(@RequestBody @Valid LectureCreateRequest request) {
        return ApiResponse.ok(lectureService.createLecture(request.toServiceRequest()));
    }

    // 특강 삭제
    @DeleteMapping("/{lectureId}")
    public ApiResponse<String> deleteLecture(@PathVariable Long lectureId) {
        lectureService.deleteLecture(lectureId);

        return ApiResponse.ok(SUCCESS_DELETE_LECTURE.message);
    }

    // 특강 신청
    @PostMapping("/apply")
    public ApiResponse<HistoryResponse> apply(@RequestBody @Valid LectureApplyRequest request) {
        return ApiResponse.ok(lectureService.applyLecture(request.toServiceRequest()));
    }

    // 특강 신청 내역에 유저가 있는지 확인
    @GetMapping("/application/{userId}")
    public ApiResponse<Boolean> checkHistories(@PathVariable Long userId, @RequestParam Long lectureId) {
        return ApiResponse.ok(lectureService.checkHistories(lectureId, userId));
    }

    //특강 신청 취소
    @PostMapping("/application/cancel")
    public ApiResponse<String> cancelLecture(@RequestBody LectureCancelRequest request) {
        lectureService.cancelHistory(request.toServiceRequest());

        return ApiResponse.ok(SUCCESS_CANCEL_LECTURE.message);
    }
}
