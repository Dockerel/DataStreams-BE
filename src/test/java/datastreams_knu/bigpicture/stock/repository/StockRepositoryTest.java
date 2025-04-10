package datastreams_knu.bigpicture.stock.repository;

import datastreams_knu.bigpicture.stock.entity.Stock;
import datastreams_knu.bigpicture.stock.entity.StockType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class StockRepositoryTest {

    @Autowired
    StockRepository stockRepository;
    @Autowired
    StockInfoRepository stockInfoRepository;

    @Autowired
    EntityManager em;

    @DisplayName("StockName에 해당하는 stock을 찾는다.")
    @Test
    void FindByStockNameTest() {
        // given
        String stockName = "testName";
        StockType stockType = StockType.KOREA;

        Stock stock = Stock.of(stockName, stockType);
        stockRepository.save(stock);

        // when
        Optional<Stock> findStock = stockRepository.findByName(stockName);

        // then
        assertThat(findStock).isPresent();
    }
}