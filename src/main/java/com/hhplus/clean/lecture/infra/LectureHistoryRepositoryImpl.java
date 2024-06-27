package com.hhplus.clean.lecture.infra;

import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import com.hhplus.clean.lecture.domain.repository.LectureHistoryRepository;
import com.hhplus.clean.lecture.exception.LectureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.hhplus.clean.lecture.exception.ErrorCode.LECTURE_HISTORY_NOT_EXIST;

@Repository
@RequiredArgsConstructor
public class LectureHistoryRepositoryImpl implements LectureHistoryRepository {

    private final LectureHistoryJpaRepository lectureHistoryJpaRepository;

    @Override
    public LectureHistory save(LectureHistory lectureHistory) {
        return lectureHistoryJpaRepository.save(lectureHistory);
    }

    @Override
    public LectureHistory findByLectureIdAndUserId(Long lectureId, Long userId) {

        Optional<LectureHistory> lectureHistory = lectureHistoryJpaRepository.findByLecture_LectureIdAndUserId(lectureId, userId);

        return lectureHistory.orElse(null);

    }

    @Override
    public boolean existsByLectureIdAndUserId(Long lectureId, Long userId) {
        return lectureHistoryJpaRepository.existsByLecture_LectureIdAndUserId(lectureId, userId);
    }

    @Override
    public void deleteByLectureIdAndUserId(Long lectureId, Long userId) {
        LectureHistory lectureHistory = lectureHistoryJpaRepository.findByLecture_LectureIdAndUserId(lectureId, userId)
                .orElseThrow(() -> new LectureException(LECTURE_HISTORY_NOT_EXIST, "취소할 강의 신청 내역이 존재하지 않습니다."));

        lectureHistoryJpaRepository.delete(lectureHistory);
    }

    @Override
    public void deleteAllInBatch() {
        lectureHistoryJpaRepository.deleteAllInBatch();

    }

    @Override
    public List<LectureHistory> findByLectureId(Long lectureId) {
        return lectureHistoryJpaRepository.findByLecture_LectureId(lectureId);
    }

}
