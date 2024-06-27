package com.hhplus.clean.lecture.infra;

import com.hhplus.clean.lecture.domain.entity.Lecture;
import com.hhplus.clean.lecture.domain.repository.LectureRepository;
import com.hhplus.clean.lecture.exception.ErrorCode;
import com.hhplus.clean.lecture.exception.LectureException;
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
//        return lectureJpaRepository.findById(lectureId);
        return lectureJpaRepository.findIdWithPessimisticLock(lectureId); //동시성 제어를 위해 DB락 사용
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
    public boolean existsByName(String name) {
        return lectureJpaRepository.existsByName(name);
    }

    @Override
    public void deleteById(Long lectureId) {

        Optional<Lecture> lecture = lectureJpaRepository.findById(lectureId);
        if (lecture.isEmpty()) throw new LectureException(ErrorCode.LECTURE_NOT_EXIST, "삭제할 특강이 존재하지 않습니다.");
        lectureJpaRepository.deleteById(lectureId);
    }
}
