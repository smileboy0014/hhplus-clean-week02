package com.hhplus.clean.lecture.domain.service;

import com.hhplus.clean.lecture.domain.entity.Lecture;
import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import com.hhplus.clean.lecture.domain.repository.LectureHistoryRepository;
import com.hhplus.clean.lecture.domain.repository.LectureRepository;
import com.hhplus.clean.lecture.domain.service.dto.*;
import com.hhplus.clean.lecture.exception.LectureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hhplus.clean.lecture.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {

    private final LectureRepository lectureRepository;

    private final LectureHistoryRepository lectureHistoryRepository;


    // 특강 목록 조회
    public List<LectureResponse> getLectures() {
        return lectureRepository.findAll()
                .stream()
                .map(LectureResponse::of)
                .toList();
    }

    // 특강 등록
    @Transactional
    public LectureResponse createLecture(LectureCreateServiceRequest request) {
        // 같은 이름의 강의가 존재하는지 확인
        validateDuplicateLectureName(request.name());

        Lecture newLecture = lectureRepository.save(request.toEntity());
        return LectureResponse.of(newLecture);
    }

    // 특강 삭제
    @Transactional
    public void deleteLecture(Long lectureId) {
        getLectureOrThrow(lectureId);
        lectureRepository.deleteById(lectureId);
    }

    // 특강 신청
    @Transactional
    public HistoryResponse applyLecture(LectureApplyServiceRequest request) {

        // 강의가 있는지 찾는다.
        Lecture lecture = getLectureOrThrow(request.lectureId());

        // 강의가 시작 전인지, 강의가 정원이 꽉 차지 않았는지 확인한다. (validation) + 강의의 정원 count 를 하나 늘린다.
        lecture.apply();

        // 유저가 강의를 신청했는지 확인한다. (validation) + 강의 신청 내역에 저장한다.
        validateDuplicateLectureHistory(request.lectureId(), request.userId());

        // 유저 강의 신청 내역 저장
        LectureHistory newHistory = lectureHistoryRepository.save(request.toEntity(lecture));
        lecture.addLectureHistory(newHistory);

        return HistoryResponse.of(newHistory);
    }

    // 특강 신청 여부 확인
    public boolean checkHistories(Long lectureId, Long userId) {
        return lectureHistoryRepository.existsByLectureIdAndUserId(lectureId, userId);
    }

    // 특강 신청 취소
    @Transactional
    public void cancelHistory(LectureCancelServiceRequest request) {
        lectureHistoryRepository.deleteByLectureIdAndUserId(request.lectureId(), request.userId());

    }

    // 이미 강의 신청한 내역이 있는지 확인
    private void validateDuplicateLectureHistory(Long lectureId, Long userId) {
        if (lectureHistoryRepository.existsByLectureIdAndUserId(lectureId, userId)) {
            throw new LectureException(DUPLICATED_LECTURE_APPLY, "이미 같은 특강에 등록한 유저가 존재합니다. '신청 특강 ID:%s, 신청 유저 ID:%s'"
                    .formatted(lectureId, userId));
        }
    }

    // 이미 같은 이름의 강의가 있는지 확인
    private void validateDuplicateLectureName(String name) {
        if (lectureRepository.existsByName(name)) {
            throw new LectureException(DUPLICATED_LECTURE_NAME, "이미 같은 이름의 강의가 존재합니다. '%s'".formatted(name));
        }
    }

    private Lecture getLectureOrThrow(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureException(LECTURE_NOT_EXIST, "특강이 존재하지 않습니다. '특강 ID:%s'".formatted(lectureId)));
    }
}
