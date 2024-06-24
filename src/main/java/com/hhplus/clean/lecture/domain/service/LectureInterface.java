package com.hhplus.clean.lecture.domain.service;

import java.util.List;

public interface LectureInterface {

    // 특강 목록 조회
    List<String> getLectures();

    // 특강 신청
    String applyLecture(Long lectureId, Long userId);

    // 특강 등록
    Long registerLecture(String request);

    // 특강 삭제
    void deleteLecture(Long lectureId);

    // 신청 여부 조회
    String checkHistory(Long lectureId, Long userId);

    // 신청 취소
    void cancelHistory(Long lectureId, Long userId);
}
