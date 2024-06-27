package com.hhplus.clean.lecture.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorCode {


    LECTURE_NOT_EXIST("강의가 존재하지 않습니다"),
    LECTURE_HISTORY_NOT_EXIST("강의 내역이 존재하지 않습니다"),
    INVALID_LECTURE_APPLY_DATE("강의 수강 기간이 유효하지 않습니다."),
    INVALID_LECTURE_APPLY_QUANTITY("강의 수강 인원이 유효하지 않습니다."),
    DUPLICATED_LECTURE_NAME("이미 같은 이름의 강의가 존재합니다."),
    DUPLICATED_LECTURE_APPLY("이미 수강 신청 내역이 존재합니다."),
    FAIL_LECTURE_APPLY_REQUEST("수강 신청에 실패하였습니다");

    public final String message;
}
