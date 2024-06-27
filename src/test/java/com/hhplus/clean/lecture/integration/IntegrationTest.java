package com.hhplus.clean.lecture.integration;

import com.hhplus.clean.lecture.domain.entity.Lecture;
import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import com.hhplus.clean.lecture.domain.repository.LectureHistoryRepository;
import com.hhplus.clean.lecture.domain.repository.LectureRepository;
import com.hhplus.clean.lecture.domain.service.LectureService;
import com.hhplus.clean.lecture.domain.service.dto.*;
import com.hhplus.clean.lecture.exception.LectureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static com.hhplus.clean.lecture.exception.ErrorCode.*;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class IntegrationTest {

    @Autowired
    LectureHistoryRepository lectureHistoryRepository;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    LectureService lectureService;

    @AfterEach
    void tearDown() {
        lectureHistoryRepository.deleteAllInBatch();
        lectureRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("수강 인원이 30명인 특강을 동시에 50명의 유저가 수강 신청하면 딱 30명만 수강 신청 완료가 된다.")
    void LectureApplyWhenConcurrency100Env() throws InterruptedException {
        // given
        int numThreads = 50;
        int expectSuccessCnt = 30;
        int expectFailCnt = 20;

        Lecture lecture = createLecture("특강", 30, 0, now());
        lectureRepository.save(lecture);

        CountDownLatch latch = new CountDownLatch(numThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        //when
        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                try {
                    long randomId = ThreadLocalRandom.current().nextLong(1, 10_000_001);
                    LectureApplyServiceRequest request = new LectureApplyServiceRequest(lecture.getLectureId(), randomId);
                    lectureService.applyLecture(request);

                    successCount.getAndIncrement();
                } catch (RuntimeException e) {
                    failCount.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        List<LectureHistory> result = lectureHistoryRepository.findByLectureId(lecture.getLectureId());

        // then
        assertThat(result).hasSize(30);
        assertThat(successCount.get()).isEqualTo(expectSuccessCnt);
        assertThat(failCount.get()).isEqualTo(expectFailCnt);
    }

    @Test
    @DisplayName("등록된 특강 목록을 조회한다.")
    void getLectures() {
        //given
        Lecture lecture = createLecture("특강", 50, 0, now());
        Lecture lecture2 = createLecture("특강2", 100, 0, now());

        lectureRepository.save(lecture);
        lectureRepository.save(lecture2);

        //when
        List<LectureResponse> result = lectureService.getLectures();

        //then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("등록된 특강 목록이 없다면 빈 배열을 반환한다.")
    void getLecturesWithNoList() {
        //given //when
        List<LectureResponse> result = lectureService.getLectures();

        //then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("수강 신청할 특강을 등록한다.")
    void createLecture() {
        // given
        Integer totalQuantity = 50;
        String name = "특강";
        LocalDateTime date = LocalDateTime.now().plusDays(10);

        LectureCreateServiceRequest request = LectureCreateServiceRequest.builder()
                .name(name)
                .totalQuantity(totalQuantity)
                .dateAppliedStart(date)
                .build();

        // when
        LectureResponse result = lectureService.createLecture(request);

        // then
        assertThat(result).extracting("name", "totalQuantity", "dateAppliedStart")
                .contains(name, totalQuantity, date);

    }

    @Test
    @DisplayName("같은 이름으로 특강을 신청하면 예외를 반환한다.")
    void createLectureWithDuplicatedName() {
        // given
        Integer totalQuantity = 50;
        String name = "특강";
        LocalDateTime date = LocalDateTime.now().plusDays(10);

        LectureCreateServiceRequest request = LectureCreateServiceRequest.builder()
                .name(name)
                .totalQuantity(totalQuantity)
                .dateAppliedStart(date)
                .build();

        Lecture lecture = createLecture(name, totalQuantity, 0, date);
        lectureRepository.save(lecture);

        // when // then
        assertThatThrownBy(() -> lectureService.createLecture(request))
                .isInstanceOf(LectureException.class)
                .extracting("errorCode")
                .isEqualTo(DUPLICATED_LECTURE_NAME);

    }

    @DisplayName("이미 등록된 특강을 삭제한다.")
    @Test
    void deleteLecture() {
        // given
        Long userId = 1L;
        Lecture lecture = createLecture("특강", 50, 0, now());
        LectureHistory lectureHistory = createHistory(lecture, userId);

        lectureRepository.save(lecture);
        lectureHistoryRepository.save(lectureHistory);

        LectureCancelServiceRequest request = LectureCancelServiceRequest.builder()
                .lectureId(lecture.getLectureId())
                .userId(userId)
                .build();

        // when
        lectureService.cancelHistory(request);
        boolean result = lectureService.checkHistories(lecture.getLectureId(), userId);

        // then
        assertThat(result).isFalse();

    }

    @DisplayName("등록되지 않은 특강을 삭제하려고 하면 예외를 반환한다.")
    @Test
    void deleteLectureWithNoLecture() {
        // given
        Long lectureId = 1L;

        // when // then
        assertThatThrownBy(() -> lectureService.deleteLecture(lectureId))
                .isInstanceOf(LectureException.class)
                .extracting("errorCode")
                .isEqualTo(LECTURE_NOT_EXIST);
    }


    @Test
    @DisplayName("유저가 수강 신청에 성공한다.")
    void applyLecture() {
        //given
        Long userId = 1L;
        Integer totalQuantity = 50;
        int applyQuantity = 1;
        String name = "특강";

        Lecture lecture = createLecture(name, totalQuantity, applyQuantity, now());
        lectureRepository.save(lecture);
        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lecture.getLectureId(), userId);


        //when
        HistoryResponse result = lectureService.applyLecture(request);

        //then
        assertThat(result).extracting("historyId", "lectureId", "userId")
                .containsExactly(result.historyId(), lecture.getLectureId(), userId);
    }

    @Test
    @DisplayName("수강 신청할 강의가 없다면 예외가 발생한다.")
    void applyLectureWithNotApplyDate() {
        //given
        Long lectureId = 1L;
        Long userId = 1L;

        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lectureId, userId);

        //when //then
        assertThatThrownBy(() -> lectureService.applyLecture(request))
                .isInstanceOf(LectureException.class)
                .extracting("errorCode")
                .isEqualTo(LECTURE_NOT_EXIST);
    }

    @Test
    @DisplayName("수강 신청할 날짜가 아직 안됐다면 예외를 반환한다.")
    void applyLectureWithNotYetApplyDate() {
        //given
        Long userId = 1L;
        Integer totalQuantity = 50;
        int applyQuantity = 1;
        String name = "특강";

        Lecture lecture = createLecture(name, totalQuantity, applyQuantity, now().plusDays(1));
        lectureRepository.save(lecture);

        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lecture.getLectureId(), userId);

        //when //then
        assertThatThrownBy(() -> lectureService.applyLecture(request))
                .isInstanceOf(LectureException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_LECTURE_APPLY_DATE);
    }

    @Test
    @DisplayName("수강 신청 정원을 초과한 경우 예외를 반환한다.")
    void applyLectureWithOverQuantity() {
        //given
        Long userId = 1L;
        Integer totalQuantity = 50;
        int applyQuantity = 50;
        String name = "특강";

        Lecture lecture = createLecture(name, totalQuantity, applyQuantity, now());
        lectureRepository.save(lecture);

        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lecture.getLectureId(), userId);

        //when //then
        assertThatThrownBy(() -> lectureService.applyLecture(request))
                .isInstanceOf(LectureException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_LECTURE_APPLY_QUANTITY);
    }

    @Test
    @DisplayName("이미 유저가 강의를 신청한 경우 예외를 반환한다.")
    void applyLectureDuplicated() {
        //given
        Long userId = 1L;
        Integer totalQuantity = 50;
        int applyQuantity = 1;
        String name = "특강";

        Lecture lecture = createLecture(name, totalQuantity, applyQuantity, now());
        lectureRepository.save(lecture);

        LectureHistory history = createHistory(lecture, userId);
        lectureHistoryRepository.save(history);

        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lecture.getLectureId(), userId);

        //when //then
        assertThatThrownBy(() -> lectureService.applyLecture(request))
                .isInstanceOf(LectureException.class)
                .extracting("errorCode")
                .isEqualTo(DUPLICATED_LECTURE_APPLY);
    }

    @Test
    @DisplayName("수강 신청에 성공한 유저는 신청 내역에서 true 를 반환한다.")
    void checkHistories() {
        //given
        Long userId = 1L;
        Integer totalQuantity = 50;
        int applyQuantity = 1;
        String name = "특강";

        Lecture lecture = createLecture(name, totalQuantity, applyQuantity, now());
        lectureRepository.save(lecture);

        LectureHistory history = createHistory(lecture, userId);
        lecture.addLectureHistory(history);
        lectureHistoryRepository.save(history);

        //when
        boolean result = lectureService.checkHistories(lecture.getLectureId(), userId);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("수강 신청에 실패한 유저는 신청 내역에서 false 를 반환한다.")
    void checkHistoriesWithFailToApplyLecture() {
        //given
        Long lectureId = 1L;
        Long userId = 1L;
        Integer totalQuantity = 50;
        int applyQuantity = 1;
        String name = "특강";

        Lecture lecture = createLecture(name, totalQuantity, applyQuantity, now());

        lectureRepository.save(lecture);

        //when
        boolean result = lectureService.checkHistories(lectureId, userId);

        //then
        assertThat(result).isFalse();
    }


    @Test
    @DisplayName("수강 신청한 특강을 취소한다.")
    void cancelLectureWithApplied() {
        // given
        Lecture lecture = createLecture("특강", 50, 5, now());

        lectureRepository.save(lecture);

        // when
        lectureService.deleteLecture(lecture.getLectureId());
        List<LectureResponse> result = lectureService.getLectures();

        // then
        assertThat(result).isEmpty();
    }

    private Lecture createLecture(String name, Integer totalQuantity, int applyQuantity, LocalDateTime dateAppliedStart) {
        return Lecture.builder()

                .name(name)
                .appliedQuantity(applyQuantity)
                .totalQuantity(totalQuantity)
                .dateAppliedStart(dateAppliedStart)
                .build();

    }

    private LectureHistory createHistory(Lecture lecture, Long userId) {
        return LectureHistory.builder()
                .lecture(lecture)
                .userId(userId)
                .dateApplied(now())
                .build();

    }


}
