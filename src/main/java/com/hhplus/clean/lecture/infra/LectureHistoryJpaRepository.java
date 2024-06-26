package com.hhplus.clean.lecture.infra;

import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureHistoryJpaRepository extends JpaRepository<LectureHistory, Long> {
    LectureHistory findByLecture_LectureIdAndUserId(Long lectureId, Long userId);

    void deleteByLecture_LectureIdAndUserId(Long lectureId, Long userId);
}
