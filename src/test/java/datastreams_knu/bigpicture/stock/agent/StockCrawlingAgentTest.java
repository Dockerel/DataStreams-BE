package datastreams_knu.bigpicture.stock.agent;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import datastreams_knu.bigpicture.stock.agent.dto.StockInfoDto;
import datastreams_knu.bigpicture.stock.entity.Stock;
import datastreams_knu.bigpicture.stock.entity.StockType;
import datastreams_knu.bigpicture.stock.repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class StockCrawlingAgentTest {

    @Autowired
    StockCrawlingAgent stockCrawlingAgent;
    @Autowired
    StockRepository stockRepository;

    @DisplayName("크롤링한 주가 데이터를 저장합니다.")
    @Test
    void SaveStockTest() {
        // given
        String type = "korea";
        String stockName = "testName";

        StockInfoDto stockInfoDto1 = StockInfoDto.of(LocalDate.now(), 0.01);
        StockInfoDto stockInfoDto2 = StockInfoDto.of(LocalDate.now(), 0.02);
        StockInfoDto stockInfoDto3 = StockInfoDto.of(LocalDate.now(), 0.03);
        List<StockInfoDto> stockInfoDtoList = List.of(stockInfoDto1, stockInfoDto2, stockInfoDto3);

        // when
        CrawlingResultDto result = stockCrawlingAgent.saveStock(type, stockName, stockInfoDtoList);

        Optional<Stock> findStock = stockRepository.findByName(stockName);

        // then
        assertThat(result.getResult()).isTrue();
        assertThat(result.getMessage()).isEqualTo("주가 크롤링 성공");

        assertThat(findStock).isPresent();
        assertThat(findStock.get().getStockType()).isEqualTo(StockType.KOREA);
        assertThat(findStock.get().getStockInfos()).hasSize(3);
    }

}