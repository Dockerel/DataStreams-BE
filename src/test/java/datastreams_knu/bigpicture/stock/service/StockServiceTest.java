package datastreams_knu.bigpicture.stock.service;

import datastreams_knu.bigpicture.stock.controller.dto.StockResponse;
import datastreams_knu.bigpicture.stock.entity.Stock;
import datastreams_knu.bigpicture.stock.entity.StockInfo;
import datastreams_knu.bigpicture.stock.entity.StockType;
import datastreams_knu.bigpicture.stock.repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StockServiceTest {

    @Autowired
    StockService stockService;
    @Autowired
    StockRepository stockRepository;

    @DisplayName("모든 주가 데이터들을 가져온다.")
    @Test
    void getAllStockInfos() {
        // given
        String stockName = "testStock";

        Stock stock = Stock.of(stockName, StockType.KOREA);
        stock.addStockInfo(StockInfo.of(0.01, LocalDate.now()
        ));

        stockRepository.save(stock);

        // when
        List<StockResponse> findStocks = stockService.getStocks(stockName);

        // then
        assertThat(findStocks).hasSize(1);
        assertThat(findStocks.get(0)).isInstanceOf(StockResponse.class);
    }
}