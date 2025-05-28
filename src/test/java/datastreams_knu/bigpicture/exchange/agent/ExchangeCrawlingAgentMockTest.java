package datastreams_knu.bigpicture.exchange.agent;

import datastreams_knu.bigpicture.common.util.WebClientUtil;
import datastreams_knu.bigpicture.exchange.agent.dto.ExchangeCrawlingDto;
import datastreams_knu.bigpicture.exchange.agent.dto.ExchangeInfoDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static datastreams_knu.bigpicture.exchange.agent.dto.ExchangeCrawlingDto.Data;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ExchangeCrawlingAgentMockTest {

    @Mock
    WebClientUtil webClientUtil;
    @InjectMocks
    ExchangeCrawlingAgent exchangeCrawlingAgent;

    @DisplayName("지난 한달 동안의 환율 데이터를 수집합니다.")
    @Test
    void ExchangeDataRequestTest() {
        // given
        List<Data> exchangeCrawlingData = List.of(
                new Data("2025-01-01", "0.01"),
                new Data("2025-01-02", "0.02"),
                new Data("2025-01-03", "0.03")
        );
        ExchangeCrawlingDto exchangeCrawlingDto = ExchangeCrawlingDto.of(exchangeCrawlingData);

        // when
        when(webClientUtil.get(any(), any()))
                .thenReturn(exchangeCrawlingDto);

        // then
        List<ExchangeInfoDto> result = exchangeCrawlingAgent.crawlingExchangeRate();
        assertThat(result).hasSize(3)
                .allSatisfy(data -> {
                    assertThat(data.getDate()).isInstanceOf(LocalDate.class);
                    assertThat(data.getRate()).isInstanceOf(Double.class);
                });
    }
}