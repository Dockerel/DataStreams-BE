package datastreams_knu.bigpicture.exchange.controller;

import datastreams_knu.bigpicture.exchange.service.ExchangeService;
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

@WebMvcTest(controllers = ExchangeController.class)
class ExchangeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ExchangeService exchangeService;

    @DisplayName("모든 환율 데이터들을 가져온다.")
    @Test
    void getExchanges() throws Exception {
        // given // when
        when(exchangeService.getExchanges())
            .thenReturn(Collections.emptyList());

        // then
        mockMvc.perform(
                get("/api/v1/exchanges")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }
}