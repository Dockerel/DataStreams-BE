package datastreams_knu.bigpicture.stock.repository;

import datastreams_knu.bigpicture.stock.entity.StockInfo;
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
class StockInfoRepositoryTest {

    @Autowired
    StockInfoRepository stockInfoRepository;

    @DisplayName("기준 날짜 이전에 수집된 주가 데이터들을 모두 삭제한다.")
    @Test
    void deleteAllByStockDateBeforeTest() {
        // given
        LocalDate now = LocalDate.now();

        List<StockInfo> stockInfos = List.of(
                StockInfo.of(0.01, now),
                StockInfo.of(0.02, now.minusDays(2)),
                StockInfo.of(0.03, now.minusDays(2))
        );
        stockInfoRepository.saveAll(stockInfos);

        // when
        int result = stockInfoRepository.deleteAllByStockDateBefore(now.minusDays(1));

        // then
        assertThat(result).isEqualTo(2);
        assertThat(stockInfoRepository.findAll()).hasSize(1);
    }

}