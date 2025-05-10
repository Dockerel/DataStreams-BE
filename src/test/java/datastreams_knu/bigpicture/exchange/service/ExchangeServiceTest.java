package datastreams_knu.bigpicture.exchange.service;

import datastreams_knu.bigpicture.exchange.controller.dto.ExchangeResponse;
import datastreams_knu.bigpicture.exchange.entity.Exchange;
import datastreams_knu.bigpicture.exchange.repository.ExchangeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ExchangeServiceTest {

    @Autowired
    ExchangeService exchangeService;
    @Autowired
    ExchangeRepository exchangeRepository;

    @DisplayName("모든 환율 데이터를 가져온다.")
    @Test
    void getAllExchanges() {
        // given
        LocalDate now = LocalDate.now();

        List<Exchange> exchanges = List.of(
            Exchange.of(now, 0.01),
            Exchange.of(now, 0.02),
            Exchange.of(now, 0.03)
        );

        exchangeRepository.saveAll(exchanges);

        // when
        List<ExchangeResponse> findExchanges = exchangeService.getExchanges();

        // then
        assertThat(findExchanges).hasSize(3);
        assertThat(findExchanges.get(0)).isInstanceOf(ExchangeResponse.class);
    }

}