package com.hhplus.clean.lecture.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "lectures")
public class Lecture extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId; //강의 ID

    @Column(nullable = false)
    private String name; // 강의명

    private Integer totalQuantity; //총 신청 인원

    @Column(nullable = false)
    private int appliedQuantity; //현 신청 인원

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL)
    private List<LectureHistory> lectureHistories;

    @Column(nullable = false)
    private LocalDateTime dateAppliedStart; //강의 신청 일시
}
