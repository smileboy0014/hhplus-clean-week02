package com.hhplus.clean.lecture.infra;

import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureHistoryJpaRepository extends JpaRepository<LectureHistory, Long> {
    Optional<LectureHistory> findByLecture_LectureIdAndUserId(Long lectureId, Long userId);

    List<LectureHistory> findByLecture_LectureId(Long lectureId);

    boolean existsByLecture_LectureIdAndUserId(Long lectureId, Long userId);

}
