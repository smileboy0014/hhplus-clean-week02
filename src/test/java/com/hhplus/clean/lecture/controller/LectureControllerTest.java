package com.hhplus.clean.lecture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.clean.lecture.domain.service.LectureService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(LectureController.class)
class LectureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LectureService lectureService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void apply() throws Exception {

    }


}