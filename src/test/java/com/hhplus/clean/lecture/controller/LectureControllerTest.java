package com.hhplus.clean.lecture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.clean.lecture.controller.dto.LectureApplyRequest;
import com.hhplus.clean.lecture.domain.entity.Lecture;
import com.hhplus.clean.lecture.domain.service.LectureService;
import com.hhplus.clean.lecture.domain.service.dto.HistoryResponse;
import com.hhplus.clean.lecture.domain.service.dto.LectureResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LectureController.class)
class LectureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LectureService lectureService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("등록된 특강 목록을 조회한다.")
    void getLectures() throws Exception {
        //given
        LectureResponse lectureResponse = LectureResponse.builder().build();
        when(lectureService.getLectures()).thenReturn(List.of(lectureResponse));

        //when //then
        mockMvc.perform(get("/lectures"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("유저가 수강 신청을 하면 수강 신청 내역이 남는다.")
    void apply() throws Exception {
        //given
        Long historyId = 1L;
        Long lectureId = 1L;
        Long userId = 1L;
        HistoryResponse response = new HistoryResponse(historyId, lectureId, userId);
        LectureApplyRequest request = new LectureApplyRequest(lectureId, userId);
        when(lectureService.applyLecture(request.toServiceRequest())).thenReturn(response);

        //when //then
        mockMvc.perform(post("/lectures/apply")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    @DisplayName("유저가 수강 신청에 성공한다.")
    void checkHistories() throws Exception {
        //given
        Long lectureId = 1L;
        Long userId = 1L;
        when(lectureService.checkHistories(lectureId, userId)).thenReturn(true);

        //when //then
        mockMvc.perform(get("/lectures/check/%s".formatted(userId))
                        .param("userId", String.valueOf(userId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value(true));
    }


}