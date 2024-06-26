package com.hhplus.clean.lecture.infra;

import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import com.hhplus.clean.lecture.domain.repository.LectureHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

        return lectureHistoryJpaRepository.findByLecture_LectureIdAndUserId(lectureId, userId);
    }

    @Override
    public void deleteByLectureIdAndUserId(Long lectureId, Long userId) {
        lectureHistoryJpaRepository.deleteByLecture_LectureIdAndUserId(lectureId, userId);
    }

}
