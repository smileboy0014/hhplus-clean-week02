package com.hhplus.clean.lecture.domain.repository;

import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureHistoryRepository {

    LectureHistory save(Long lectureId, Long userId);

    void deleteAll();

    void deleteByLectureIdAndUserId(Long lectureId, Long userId);

    void deleteByLectureId(Long lectureId);
}
