package com.hhplus.clean.lecture.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hhplus.clean.lecture.exception.LectureException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.hhplus.clean.lecture.exception.ErrorCode.INVALID_LECTURE_APPLY_DATE;
import static com.hhplus.clean.lecture.exception.ErrorCode.INVALID_LECTURE_APPLY_QUANTITY;
import static java.time.LocalDateTime.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "lectures")
public class Lecture extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long lectureId; //강의 ID

    @Column(nullable = false)
    private String name; // 강의명

    private Integer totalQuantity; //총 신청 인원

    @Column(nullable = false)
    private int appliedQuantity; //현 신청 인원

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL)
    private List<LectureHistory> lectureHistories; //강의 신청 내역

    @Column(nullable = false)
    private LocalDateTime dateAppliedStart; //강의 신청 일시


    //== 연관 관계 편의 메서드 ==//
//    public void setLectureHistory(LectureHistory lectureHistory) {
//        if (this.lectureHistories == null) {
//            this.lectureHistories = new ArrayList<>();
//        }
//
//        this.lectureHistories.add(lectureHistory);
//
//        if (lectureHistory.getLecture() != this) {
//            lectureHistory.belongTo(this);
//        }
//    }

    // 검증 로직
    public void apply() {
        // 기간이 아직 안됐는지 확인
        if (!availableApplyDate()) {
            throw new LectureException(INVALID_LECTURE_APPLY_DATE, "아직 특강 신청 기간이 되지 않았습니다.");
        }
        // 정원이 다 안 찼는지 확인
        if (!availableApplyQuantity()) {
            throw new LectureException(INVALID_LECTURE_APPLY_QUANTITY, "정원이 다 찼습니다.");
        }
        appliedQuantity++;
    }

    // 특강 시작 전인지 확인
    private boolean availableApplyDate() {
        return dateAppliedStart.isBefore(now());
    }

    // 특강 정원이 다 찼는지 확인
    private boolean availableApplyQuantity() {

        if (totalQuantity == null) {
            return true;
        }

        return appliedQuantity < totalQuantity;
    }


}
