package datastreams_knu.bigpicture.exchange.agent;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import datastreams_knu.bigpicture.exchange.agent.dto.ExchangeInfoDto;
import datastreams_knu.bigpicture.exchange.entity.Exchange;
import datastreams_knu.bigpicture.exchange.repository.ExchangeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ExchangeCrawlingAgentTest {

    @Autowired
    ExchangeCrawlingAgent exchangeCrawlingAgent;
    @Autowired
    ExchangeRepository exchangeRepository;

    @DisplayName("수집된 환율 데이터를 DB에 저장한다.")
    @Test
    void ExchangeDataSaveTest() {
        // given
        List<ExchangeInfoDto> exchangeData = List.of(
            ExchangeInfoDto.of(LocalDate.now(), 0.01),
            ExchangeInfoDto.of(LocalDate.now(), 0.02),
            ExchangeInfoDto.of(LocalDate.now(), 0.03)
        );

        // when
        CrawlingResultDto result = exchangeCrawlingAgent.saveExchange(exchangeData);
        List<Exchange> findExchange = exchangeRepository.findAll();

        // then
        assertThat(result.getResult()).isEqualTo(true);
        assertThat(result.getMessage()).isEqualTo("환율 크롤링 성공");
        assertThat(findExchange).hasSize(3);
    }
}