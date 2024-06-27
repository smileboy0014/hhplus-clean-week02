package com.hhplus.clean.lecture.domain.repository;

import com.hhplus.clean.lecture.domain.entity.Lecture;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository {

    Optional<Lecture> findById(Long lectureId);

    Lecture save(Lecture lecture);

    List<Lecture> findAll();

    boolean existsByName(String name);

    void deleteById(Long lectureId);

    void deleteAllInBatch();

}
