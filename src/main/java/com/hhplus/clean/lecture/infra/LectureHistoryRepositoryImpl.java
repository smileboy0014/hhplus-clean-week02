package com.hhplus.clean.lecture.infra;

import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import com.hhplus.clean.lecture.domain.repository.LectureHistoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LectureHistoryRepositoryImpl implements LectureHistoryRepository {
    @Override
    public LectureHistory save(Long lectureId, Long userId) {
        return null;
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
