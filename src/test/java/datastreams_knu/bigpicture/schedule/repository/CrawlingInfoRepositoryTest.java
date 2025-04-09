package datastreams_knu.bigpicture.schedule.repository;

import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class CrawlingInfoRepositoryTest {

    @Autowired
    CrawlingInfoRepository crawlingInfoRepository;

    String existStockName = "stockName";

    @BeforeEach
    void setup() {
        CrawlingInfo crawlingInfo = CrawlingInfo.of("stockType", existStockName, "newsKeyword");
        crawlingInfoRepository.save(crawlingInfo);
    }

    @DisplayName("CrawlingInfoRepository에서 stockName으로 CrawlingInfo를 가져옵니다.")
    @Test
    void findExistStockNameTest() {
        // given // when
        Optional<CrawlingInfo> findStockName = crawlingInfoRepository.findByStockName(existStockName);

        // then
        assertThat(findStockName).isPresent();
    }

    @DisplayName("CrawlingInfoRepository에서 존재하지 않는 stockName으로 조회하면 Optional.empty를 반환합니다.")
    @Test
    void findNonexistentStockNameTest() {
        // given
        String nonexistentStockName = "nonexistentStockName";

        // when
        Optional<CrawlingInfo> findStockName = crawlingInfoRepository.findByStockName(nonexistentStockName);

        // then
        assertThat(findStockName).isEmpty();
    }
}