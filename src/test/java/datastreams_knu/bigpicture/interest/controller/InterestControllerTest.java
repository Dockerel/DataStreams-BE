package datastreams_knu.bigpicture.interest.controller;

import datastreams_knu.bigpicture.interest.service.InterestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InterestController.class)
class InterestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    InterestService interestService;

    @DisplayName("모든 한국 금리 데이터들을 가져온다.")
    @Test
    void getKoreaInterests() throws Exception {
        // given // when
        when(interestService.getKoreaInterests())
            .thenReturn(Collections.emptyList());

        // then
        mockMvc.perform(
                get("/api/v1/interests/korea")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("모든 미국 금리 데이터들을 가져온다.")
    @Test
    void getUSInterests() throws Exception {
        // given // when
        when(interestService.getUSInterests())
            .thenReturn(Collections.emptyList());

        // then
        mockMvc.perform(
                get("/api/v1/interests/us")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

}