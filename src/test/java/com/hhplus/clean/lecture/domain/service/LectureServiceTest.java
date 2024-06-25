package com.hhplus.clean.lecture.domain.service;

import com.hhplus.clean.lecture.domain.entity.Lecture;
import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import com.hhplus.clean.lecture.domain.repository.LectureHistoryRepository;
import com.hhplus.clean.lecture.domain.repository.LectureRepository;
import com.hhplus.clean.lecture.domain.service.dto.HistoryResponse;
import com.hhplus.clean.lecture.domain.service.dto.LectureApplyServiceRequest;
import com.hhplus.clean.lecture.domain.service.dto.LectureResponse;
import com.hhplus.clean.lecture.exception.LectureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.hhplus.clean.lecture.exception.ErrorCode.*;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


class LectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private LectureHistoryRepository lectureHistoryRepository;

    @InjectMocks
    private LectureService lectureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("등록된 특강 목록을 조회한다.")
    void getLectures() {
        //given
        Lecture lecture = createLecture(1L, "특강", 50, 0, now());
        Lecture lecture2 = createLecture(2L, "특강2", 100, 0, now());
        when(lectureRepository.findAll()).thenReturn(List.of(lecture, lecture2));

        //when
        List<LectureResponse> result = lectureService.getLectures();

        //then
        assertThat(result).hasSize(2);
    }


    @Test
    @DisplayName("유저가 수강 신청에 성공한다.")
    void applyLecture() {
        //given
        Long lectureId = 1L;
        Long userId = 1L;
        Integer totalQuantity = 50;
        int applyQuantity = 1;
        String name = "특강";

        Lecture lecture = createLecture(lectureId, name, totalQuantity, applyQuantity, now());
        LectureHistory lectureHistory = createHistory(lecture, userId);
        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lectureId, userId);

        when(lectureRepository.findById(request.lectureId())).thenReturn(Optional.ofNullable(lecture));
        when(lectureHistoryRepository.findByLectureIdAndUserId(lectureId, userId)).thenReturn(null);
        when(lectureHistoryRepository.save(request.toEntity(lecture))).thenReturn(lectureHistory);

        //when
        HistoryResponse result = lectureService.applyLecture(request);

        //then
        assertThat(result).extracting("lectureId", "userId")
                .containsExactly(lectureId, userId);
    }

    @Test
    @DisplayName("수강 신청할 강의가 없다면 예외가 발생한다.")
    void applyLectureWithNotApplyDate() {
        //given
        Long lectureId = 1L;
        Long userId = 1L;
        Integer totalQuantity = 50;
        int applyQuantity = 1;
        String name = "특강";

        Lecture lecture = createLecture(lectureId, name, totalQuantity, applyQuantity, now());
        LectureHistory lectureHistory = createHistory(lecture, userId);
        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lectureId, userId);

        when(lectureRepository.findById(request.lectureId())).thenReturn(Optional.empty());
        when(lectureHistoryRepository.findByLectureIdAndUserId(lectureId, userId)).thenReturn(null);
        when(lectureHistoryRepository.save(request.toEntity(lecture))).thenReturn(lectureHistory);

        //when //then
        assertThatThrownBy(() -> lectureService.applyLecture(request))
                .isInstanceOf(LectureException.class)
                .extracting("errorCode")
                .isEqualTo(LECTURE_NOT_EXIST);
    }

    @Test
    @DisplayName("수강 신청할 날짜가 아직 안됐다면 예외를 반환한다.")
    void applyLectureWithNotYetApplyDate() {
        Long lectureId = 1L;
        Long userId = 1L;
        Integer totalQuantity = 50;
        int applyQuantity = 1;
        String name = "특강";

        Lecture lecture = createLecture(lectureId, name, totalQuantity, applyQuantity, now().plusDays(1));
        LectureHistory lectureHistory = createHistory(lecture, userId);
        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lectureId, userId);

        when(lectureRepository.findById(request.lectureId())).thenReturn(Optional.ofNullable(lecture));
        when(lectureHistoryRepository.findByLectureIdAndUserId(lectureId, userId)).thenReturn(null);
        when(lectureHistoryRepository.save(request.toEntity(lecture))).thenReturn(lectureHistory);

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
        Long lectureId = 1L;
        Long userId = 1L;
        Integer totalQuantity = 50;
        int applyQuantity = 50;
        String name = "특강";

        Lecture lecture = createLecture(lectureId, name, totalQuantity, applyQuantity, now());
        LectureHistory lectureHistory = createHistory(lecture, userId);
        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lectureId, userId);

        when(lectureRepository.findById(request.lectureId())).thenReturn(Optional.ofNullable(lecture));
        when(lectureHistoryRepository.findByLectureIdAndUserId(lectureId, userId)).thenReturn(null);
        when(lectureHistoryRepository.save(request.toEntity(lecture))).thenReturn(lectureHistory);

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
        Long lectureId = 1L;
        Long userId = 1L;
        Integer totalQuantity = 50;
        int applyQuantity = 1;
        String name = "특강";

        Lecture lecture = createLecture(lectureId, name, totalQuantity, applyQuantity, now());
        LectureHistory lectureHistory1 = createHistory(lecture, userId);
        LectureHistory lectureHistory2 = createHistory(lecture, userId);
        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lectureId, userId);

        when(lectureRepository.findById(request.lectureId())).thenReturn(Optional.ofNullable(lecture));
        when(lectureHistoryRepository.findByLectureIdAndUserId(lectureId, userId)).thenReturn(lectureHistory1);
        when(lectureHistoryRepository.save(request.toEntity(lecture))).thenReturn(lectureHistory2);

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
        Long userId = 2L;
        Long lectureId = 2L;
        Lecture lecture = Lecture.builder().build();

        LectureHistory lectureHistory = createHistory(lecture, userId);
        when(lectureHistoryRepository.findByLectureIdAndUserId(lectureId, userId)).thenReturn(lectureHistory);

        //when
        boolean result = lectureService.checkHistories(lectureId, userId);

        //then
        assertThat(result).isEqualTo(true);
    }

    private Lecture createLecture(Long lectureId, String name, Integer totalQuantity, int applyQuantity, LocalDateTime dateAppliedStart) {
        return Lecture.builder()
                .lectureId(lectureId)
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
                .build();

    }

}