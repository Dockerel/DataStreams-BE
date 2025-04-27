package datastreams_knu.bigpicture.interest.repository;

import datastreams_knu.bigpicture.interest.entity.KoreaInterest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class KoreaInterestRepositoryTest {

    @Autowired
    KoreaInterestRepository koreaInterestRepository;

    @DisplayName("")
    @Test
    void test() {
        // given
        List<KoreaInterest> koreaInterests = List.of(
            KoreaInterest.of(LocalDate.now(), 0.01),
            KoreaInterest.of(LocalDate.now().minusMonths(2), 0.02),
            KoreaInterest.of(LocalDate.now().minusMonths(2), 0.03)
        );
        koreaInterestRepository.saveAll(koreaInterests);

        // when
        int result = koreaInterestRepository.deleteAllByInterestDateBefore(LocalDate.now().minusMonths(1));

        // then
        assertThat(result).isEqualTo(2);
        assertThat(koreaInterestRepository.findAll()).hasSize(1);
    }
}