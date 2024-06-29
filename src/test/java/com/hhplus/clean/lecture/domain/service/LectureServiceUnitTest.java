package com.hhplus.clean.lecture.domain.service;

import com.hhplus.clean.lecture.domain.entity.Lecture;
import com.hhplus.clean.lecture.domain.entity.LectureHistory;
import com.hhplus.clean.lecture.domain.repository.LectureHistoryRepository;
import com.hhplus.clean.lecture.domain.repository.LectureRepository;
import com.hhplus.clean.lecture.domain.service.dto.*;
import com.hhplus.clean.lecture.exception.LectureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hhplus.clean.lecture.exception.ErrorCode.*;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class LectureServiceUnitTest {

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
    @DisplayName("등록된 특강 목록이 없다면 빈 배열을 반환한다.")
    void getLecturesWithNoList() {
        //given
        when(lectureRepository.findAll()).thenReturn(new ArrayList<>());

        //when
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

        Lecture lecture = createLecture(1L, name, totalQuantity, 0, date);

        when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

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

        Lecture lecture = createLecture(1L, name, totalQuantity, 0, date);

        when(lectureRepository.existsByName(name)).thenReturn(true);
        when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

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
        Long lectureId = 1L;
        Lecture lecture = Lecture.builder().build();

        // when
        when(lectureRepository.findById(lectureId)).thenReturn(Optional.ofNullable(lecture));
        lectureService.deleteLecture(lectureId);

        // then
        verify(lectureRepository).deleteById(lectureId);

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
        Long lectureId = 1L;
        Long userId = 1L;
        Long historyId = 1L;
        Integer totalQuantity = 50;
        int applyQuantity = 1;
        String name = "특강";

        Lecture lecture = createLecture(lectureId, name, totalQuantity, applyQuantity, now());
        LectureHistory lectureHistory = createHistory(historyId, lecture, userId);
        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lectureId, userId);

        when(lectureRepository.findById(request.lectureId())).thenReturn(Optional.ofNullable(lecture));
        when(lectureHistoryRepository.findByLectureIdAndUserId(lectureId, userId)).thenReturn(null);
        when(lectureHistoryRepository.save(any(LectureHistory.class))).thenReturn(lectureHistory);

        //when
        HistoryResponse result = lectureService.applyLecture(request);

        //then
        assertThat(result).extracting("historyId", "lectureId", "userId")
                .containsExactly(historyId, lectureId, userId);
    }

    @Test
    @DisplayName("수강 신청할 강의가 없다면 예외가 발생한다.")
    void applyLectureWithNotApplyDate() {
        //given
        Long lectureId = 1L;
        Long userId = 1L;

        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lectureId, userId);

        when(lectureRepository.findById(request.lectureId())).thenReturn(Optional.empty());

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
        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lectureId, userId);

        when(lectureRepository.findById(request.lectureId())).thenReturn(Optional.ofNullable(lecture));

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
        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lectureId, userId);

        when(lectureRepository.findById(request.lectureId())).thenReturn(Optional.ofNullable(lecture));

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
        LectureApplyServiceRequest request = new LectureApplyServiceRequest(lectureId, userId);

        when(lectureRepository.findById(request.lectureId())).thenReturn(Optional.ofNullable(lecture));
        when(lectureHistoryRepository.existsByLectureIdAndUserId(lectureId, userId)).thenReturn(true);

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
        Long lectureId = 1L;

        when(lectureHistoryRepository.existsByLectureIdAndUserId(lectureId, userId)).thenReturn(true);

        //when
        boolean result = lectureService.checkHistories(lectureId, userId);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("수강 신청에 실패한 유저는 신청 내역에서 false 를 반환한다.")
    void checkHistoriesWithFailToApplyLecture() {
        //given
        Long userId = 1L;
        Long lectureId = 1L;

        when(lectureHistoryRepository.existsByLectureIdAndUserId(lectureId, userId)).thenReturn(false);

        //when
        boolean result = lectureService.checkHistories(lectureId, userId);

        //then
        assertThat(result).isFalse();
    }


    @Test
    @DisplayName("수강 신청한 특강을 취소한다.")
    void cancelLectureWithApplied() {
        // given
        LectureCancelServiceRequest request = LectureCancelServiceRequest.builder().build();

        // when
        lectureService.cancelHistory(request);

        // then
        verify(lectureHistoryRepository).deleteByLectureIdAndUserId(request.lectureId(), request.userId());
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

    private LectureHistory createHistory(Long historyId, Lecture lecture, Long userId) {
        return LectureHistory.builder()
                .historyId(historyId)
                .lecture(lecture)
                .userId(userId)
                .build();

    }

}