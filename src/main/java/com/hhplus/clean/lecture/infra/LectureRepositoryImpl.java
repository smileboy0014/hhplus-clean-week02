package com.hhplus.clean.lecture.infra;

import com.hhplus.clean.lecture.domain.entity.Lecture;
import com.hhplus.clean.lecture.domain.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository {

    private final LectureJpaRepository lectureJpaRepository;

    @Override
    public Optional<Lecture> findById(Long lectureId) {
        return lectureJpaRepository.findById(lectureId);
    }

    @Override
    public Lecture save(Lecture lecture) {
        return lectureJpaRepository.save(lecture);
    }

    @Override
    public List<Lecture> findAll() {
        return lectureJpaRepository.findAll();
    }

    @Override
    public Optional<Lecture> findByName(String name) {
        return lectureJpaRepository.findByName(name);
    }

    @Override
    public void deleteById(Long lectureId) {

        lectureJpaRepository.findById(lectureId)
                .ifPresent(lectureJpaRepository::delete);
    }
}
