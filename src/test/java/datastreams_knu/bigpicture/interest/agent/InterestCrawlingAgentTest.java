package datastreams_knu.bigpicture.interest.agent;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import datastreams_knu.bigpicture.interest.agent.dto.KoreaInterestCrawlingDto;
import datastreams_knu.bigpicture.interest.entity.KoreaInterest;
import datastreams_knu.bigpicture.interest.repository.KoreaInterestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static datastreams_knu.bigpicture.interest.agent.dto.KoreaInterestCrawlingDto.StatisticRow;
import static datastreams_knu.bigpicture.interest.agent.dto.KoreaInterestCrawlingDto.of;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class InterestCrawlingAgentTest {

    @Autowired
    InterestCrawlingAgent interestCrawlingAgent;
    @Autowired
    KoreaInterestRepository koreaInterestRepository;

    @DisplayName("수집된 한국 금리 데이터를 DB에 저장한다.")
    @Test
    void KoreaInterestDataSaveTest() {
        // given
        List<StatisticRow> statisticRows = List.of(
            new StatisticRow("2025-01-01", "0.01"),
            new StatisticRow("2025-01-02", "0.02"),
            new StatisticRow("2025-01-03", "0.03")
        );

        KoreaInterestCrawlingDto koreaInterestData = of(statisticRows);

        // when
        CrawlingResultDto result = interestCrawlingAgent.saveKoreaInterest(koreaInterestData);
        List<KoreaInterest> findKoreaInterest = koreaInterestRepository.findAll();

        // then
        assertThat(result.getResult()).isEqualTo(true);
        assertThat(result.getMessage()).isEqualTo("한국 금리 크롤링 성공");
        assertThat(findKoreaInterest).hasSize(3)
            .allSatisfy(interest -> {
                assertThat(interest.getInterestDate()).isInstanceOf(LocalDate.class);
                assertThat(interest.getInterestRate()).isInstanceOf(Double.class);
            });
    }
}