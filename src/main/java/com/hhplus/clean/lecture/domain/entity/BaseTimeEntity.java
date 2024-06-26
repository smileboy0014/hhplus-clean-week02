package com.hhplus.clean.lecture.domain.entity;


import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    //나노초까지 저장되는 것때문에 수정
//    @PrePersist
//    public void onPrePersist() {
//        this.createdAt = truncateToSeconds(LocalDateTime.now());
//        this.updatedAt = this.createdAt;
//    }
//
//    //나노초까지 저장되는 것때문에 수정
//    @PreUpdate
//    public void onPreUpdate() {
//        this.updatedAt = truncateToSeconds(LocalDateTime.now());
//    }
//
//    private LocalDateTime truncateToSeconds(LocalDateTime dateTime) {
//        return dateTime.truncatedTo(ChronoUnit.SECONDS);
//    }

}
