package datastreams_knu.bigpicture.exchange.repository;

import datastreams_knu.bigpicture.exchange.entity.Exchange;
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
class ExchangeRepositoryTest {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @DisplayName("기준 날짜 이전에 수집된 환율 데이터들을 모두 삭제한다.")
    @Test
    void deleteAllByExchangeDateBeforeTest() {
        // given
        List<Exchange> exchanges = List.of(
            Exchange.of(LocalDate.now(), 0.01),
            Exchange.of(LocalDate.now().minusMonths(2), 0.02),
            Exchange.of(LocalDate.now().minusMonths(2), 0.03)
        );
        exchangeRepository.saveAll(exchanges);

        // when
        int result = exchangeRepository.deleteAllByExchangeDateBefore(LocalDate.now().minusMonths(1));

        // then
        assertThat(result).isEqualTo(2);
        assertThat(exchangeRepository.findAll()).hasSize(1);
    }
}