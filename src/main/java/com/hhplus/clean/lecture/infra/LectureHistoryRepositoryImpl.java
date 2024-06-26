package com.hhplus.clean.lecture.infra;

import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import com.hhplus.clean.lecture.domain.repository.LectureHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public List<LectureHistory> findAll() {
        return lectureHistoryJpaRepository.findAll();
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteByLectureIdAndUserId(Long lectureId, Long userId) {

    }

    @Override
    public void deleteByLectureId(Long lectureId) {

    }
}
