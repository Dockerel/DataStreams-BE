package datastreams_knu.bigpicture.schedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import datastreams_knu.bigpicture.schedule.controller.dto.RegisterCrawlingDataRequest;
import datastreams_knu.bigpicture.schedule.controller.dto.RegisterCrawlingDataResponse;
import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import datastreams_knu.bigpicture.schedule.service.SchedulerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    SchedulerService schedulerService;

    @DisplayName("크롤링을 위한 데이터를 등록한다.")
    @Test
    void registerCrawlingDataTest() throws Exception {
        // given
        String testStockType = "korea";
        RegisterCrawlingDataRequest request = RegisterCrawlingDataRequest.of(testStockType, "testStockName");

        CrawlingInfo crawlingInfo = CrawlingInfo.of(testStockType, "testStockName", "testNewsKeyword");
        RegisterCrawlingDataResponse response = RegisterCrawlingDataResponse.from(crawlingInfo);

        // when
        when(schedulerService.registerCrawlingData(any()))
                .thenReturn(response);

        // then
        mockMvc.perform(
                        post("/api/v1/schedule/register")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("크롤링을 위한 데이터를 등록할 떄 stockType은 필수이다.")
    @Test
    void registerCrawlingDataStockTypeBlankTest() throws Exception {
        // given
        RegisterCrawlingDataRequest request = RegisterCrawlingDataRequest.of("", "testStockName");

        // when
        when(schedulerService.registerCrawlingData(any()))
                .thenReturn(new RegisterCrawlingDataResponse());

        // then
        mockMvc.perform(
                        post("/api/v1/schedule/register")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    @DisplayName("크롤링을 위한 데이터를 등록할 떄 stockName은 필수이다.")
    @Test
    void registerCrawlingDataStockNameBlankTest() throws Exception {
        // given
        RegisterCrawlingDataRequest request = RegisterCrawlingDataRequest.of("testStockType", "");

        // when
        when(schedulerService.registerCrawlingData(any()))
                .thenReturn(new RegisterCrawlingDataResponse());

        // then
        mockMvc.perform(
                        post("/api/v1/schedule/register")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    @DisplayName("크롤링을 위한 데이터를 등록할 떄 stockType은 'korea' 혹은 'us'만 허용된다.")
    @Test
    void registerCrawlingDataStockTypeTest() throws Exception {
        // given
        String[] wrongStockTypes = {
                "usa",
                "koree",
                "",
                "u-s",
                "ko"
        };

        // when
        when(schedulerService.registerCrawlingData(any()))
                .thenReturn(new RegisterCrawlingDataResponse());

        // then
        for (String wrongStockType : wrongStockTypes) {
            RegisterCrawlingDataRequest request = RegisterCrawlingDataRequest.of(wrongStockType, "testStockName");
            mockMvc.perform(
                            post("/api/v1/schedule/register")
                                    .content(objectMapper.writeValueAsString(request))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("400"))
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
        }
    }
}