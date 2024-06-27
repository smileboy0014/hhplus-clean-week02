package com.hhplus.clean.lecture.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "lecture_histories")
public class LectureHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @Column(nullable = false)
    private Long userId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime dateApplied;

    //== 연관 관계 편의 메서드 ==//
    public void belongTo(Lecture lecture) {
        if (this.lecture != null) {
            this.lecture.getLectureHistories().remove(this);
        }
        this.lecture = lecture;
    }
}
