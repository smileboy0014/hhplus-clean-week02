package com.hhplus.clean.lecture.domain.repository;

import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureHistoryRepository {

    LectureHistory save(LectureHistory lectureHistory);

    LectureHistory findByLectureIdAndUserId(Long lectureId, Long userId);


    List<LectureHistory> findAll();

    void deleteAll();

    void deleteByLectureIdAndUserId(Long lectureId, Long userId);

    void deleteByLectureId(Long lectureId);
}
