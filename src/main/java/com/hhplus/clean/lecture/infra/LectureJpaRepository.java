package com.hhplus.clean.lecture.infra;

import com.hhplus.clean.lecture.domain.entity.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {


    @Query("select l from Lecture l left join fetch l.lectureHistories")
    List<Lecture> findAllWithFetchJoin();

    Optional<Lecture> findByName(String name);

    //비관적 락 사용
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from Lecture  l where l.lectureId = :id")
    Optional<Lecture> findIdWithPessimisticLock(Long id);

    boolean existsByName(String name);
}
