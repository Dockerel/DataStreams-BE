package datastreams_knu.bigpicture.interest.service;

import datastreams_knu.bigpicture.interest.controller.dto.InterestResponse;
import datastreams_knu.bigpicture.interest.entity.KoreaInterest;
import datastreams_knu.bigpicture.interest.entity.USInterest;
import datastreams_knu.bigpicture.interest.repository.KoreaInterestRepository;
import datastreams_knu.bigpicture.interest.repository.USInterestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class InterestServiceTest {

    @Autowired
    InterestService interestService;
    @Autowired
    KoreaInterestRepository koreaInterestRepository;
    @Autowired
    USInterestRepository usInterestRepository;

    @DisplayName("모든 한국 금리 데이터를 가져온다.")
    @Test
    void getAllKoreaInterests() {
        // given
        LocalDate now = LocalDate.now();

        List<KoreaInterest> koreaInterests = List.of(
            KoreaInterest.of(now, 0.01),
            KoreaInterest.of(now, 0.02),
            KoreaInterest.of(now, 0.03)
        );

        koreaInterestRepository.saveAll(koreaInterests);

        // when
        List<InterestResponse> findKoreaInterests = interestService.getKoreaInterests();

        // then
        assertThat(findKoreaInterests).hasSize(3);
        assertThat(findKoreaInterests.get(0)).isInstanceOf(InterestResponse.class);
    }

    @DisplayName("모든 미국 금리 데이터를 가져온다.")
    @Test
    void getAllUSInterests() {
        // given
        LocalDate now = LocalDate.now();

        List<USInterest> usInterests = List.of(
            USInterest.of(now, 0.01),
            USInterest.of(now, 0.02),
            USInterest.of(now, 0.03)
        );

        usInterestRepository.saveAll(usInterests);

        // when
        List<InterestResponse> findUSInterests = interestService.getUSInterests();

        // then
        assertThat(findUSInterests).hasSize(3);
        assertThat(findUSInterests.get(0)).isInstanceOf(InterestResponse.class);
    }

}