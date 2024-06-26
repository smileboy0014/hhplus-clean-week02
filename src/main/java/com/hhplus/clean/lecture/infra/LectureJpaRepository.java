package com.hhplus.clean.lecture.infra;

import com.hhplus.clean.lecture.domain.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {
    Optional<Lecture> findByName(String name);
}
