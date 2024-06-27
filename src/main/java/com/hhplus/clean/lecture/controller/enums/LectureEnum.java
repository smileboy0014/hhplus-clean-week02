package com.hhplus.clean.lecture.controller.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LectureEnum {

    SUCCESS_DELETE_LECTURE("강의를 삭제하는데 성공하였습니다."),
    SUCCESS_CANCEL_LECTURE("강의를 취소하는데 성공하였습니다.");

    public final String message;
}
