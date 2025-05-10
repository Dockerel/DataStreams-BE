package datastreams_knu.bigpicture.stock.controller;

import datastreams_knu.bigpicture.stock.service.StockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StockController.class)
class StockControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    StockService stockService;

    @DisplayName("모든 주가 데이터들을 가져온다.")
    @Test
    void getStocks() throws Exception {
        // given // when
        when(stockService.getStocks(any()))
            .thenReturn(Collections.emptyList());

        // then
        mockMvc.perform(
                get("/api/v1/stocks")
                    .param("stockName", "test")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

}