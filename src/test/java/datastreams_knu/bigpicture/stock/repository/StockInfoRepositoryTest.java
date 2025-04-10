package datastreams_knu.bigpicture.stock.repository;

import datastreams_knu.bigpicture.stock.entity.Stock;
import datastreams_knu.bigpicture.stock.entity.StockInfo;
import datastreams_knu.bigpicture.stock.entity.StockType;
import jakarta.persistence.EntityManager;
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
class StockInfoRepositoryTest {

    @Autowired
    StockRepository stockRepository;
    @Autowired
    StockInfoRepository stockInfoRepository;

    @Autowired
    EntityManager em;

    @DisplayName("StockId에 해당하는 stockInfo들을 삭제한다.")
    @Test
    void DeleteAllStockInfosByStockIdTest() {
        // given
        Stock stock = Stock.of("testName", StockType.KOREA);

        StockInfo stockInfo1 = StockInfo.of(0.01, LocalDate.now());
        StockInfo stockInfo2 = StockInfo.of(0.02, LocalDate.now());
        StockInfo stockInfo3 = StockInfo.of(0.03, LocalDate.now());

        stock.addStockInfo(stockInfo1);
        stock.addStockInfo(stockInfo2);
        stock.addStockInfo(stockInfo3);

        stockRepository.save(stock);

        // when
        stockInfoRepository.deleteAllByStockId(stock.getId());
        em.flush();
        em.clear();

        // then
        Optional<Stock> findStock = stockRepository.findById(stock.getId());
        assertThat(findStock.isPresent()).isTrue();
        assertThat(findStock.get().getStockInfos()).hasSize(0);
    }

}