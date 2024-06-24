package com.hhplus.clean.lecture.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService implements LectureInterface {


    @Override
    public List<String> getLectures() {
        return List.of();
    }

    @Override
    public String applyLecture(Long lectureId, Long userId) {
        return "";
    }

    @Override
    public Long registerLecture(String request) {
        return 0L;
    }

    @Override
    public void deleteLecture(Long lectureId) {

    }

    @Override
    public String checkHistory(Long lectureId, Long userId) {
        return "";
    }

    @Override
    public void cancelHistory(Long lectureId, Long userId) {

    }
}
