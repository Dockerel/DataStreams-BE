package datastreams_knu.bigpicture.stock.agent;

import datastreams_knu.bigpicture.common.util.WebClientUtil;
import datastreams_knu.bigpicture.stock.agent.dto.KoreaStockCrawlingDto;
import datastreams_knu.bigpicture.stock.agent.dto.StockInfoDto;
import datastreams_knu.bigpicture.stock.agent.dto.USStockCrawlingDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static datastreams_knu.bigpicture.stock.agent.dto.KoreaStockCrawlingDto.Item;
import static datastreams_knu.bigpicture.stock.agent.dto.USStockCrawlingDto.Result;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class StockCrawlingAgentMockTest {

    @Mock
    WebClientUtil webClientUtil;
    @InjectMocks
    StockCrawlingAgent stockCrawlingAgent;

    @DisplayName("한국 주식의 주가 데이터를 수집합니다.")
    @Test
    void KoreaStockDataRequestTest() {
        // given
        String stockName = "testName";

        List<Item> StockCrawlingData = List.of(
            Item.of("20250101", "0.01"),
            Item.of("20250102", "0.02"),
            Item.of("20250103", "0.03")
        );

        KoreaStockCrawlingDto koreaStockCrawlingDto = KoreaStockCrawlingDto.of(StockCrawlingData);

        // when
        when(webClientUtil.get(any(), any()))
            .thenReturn(koreaStockCrawlingDto);

        // then
        List<StockInfoDto> result = stockCrawlingAgent.crawlingKoreaStockByName(stockName);
        assertThat(result).hasSize(3)
            .allSatisfy(data -> {
                assertThat(data.getStockDate()).isInstanceOf(LocalDate.class);
                assertThat(data.getStockPrice()).isInstanceOf(Double.class);
            });
    }

    @DisplayName("미국 주식의 주가 데이터를 수집합니다.")
    @Test
    void USStockDataRequestTest() {
        // given
        String stockName = "testName";

        List<Result> StockCrawlingData = List.of(
            Result.of(0.01, 123456789L),
            Result.of(0.02, 123456789L),
            Result.of(0.03, 123456789L)
        );

        USStockCrawlingDto usStockCrawlingDto = USStockCrawlingDto.of(StockCrawlingData);

        // when
        when(webClientUtil.get(any(), any()))
            .thenReturn(usStockCrawlingDto);

        // then
        List<StockInfoDto> result = stockCrawlingAgent.crawlingUSStockByTicker(stockName);
        assertThat(result).hasSize(3)
            .allSatisfy(data -> {
                assertThat(data.getStockDate()).isInstanceOf(LocalDate.class);
                assertThat(data.getStockPrice()).isInstanceOf(Double.class);
            });
    }
}