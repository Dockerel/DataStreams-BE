package datastreams_knu.bigpicture.ecos;

import datastreams_knu.bigpicture.ecos.domain.KeyStatisticEntity;
import datastreams_knu.bigpicture.ecos.repository.KeyStatisticRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class EcosRepository {

    @Autowired
    private KeyStatisticRepository keyStatisticRepository;

    @Test
    void test() {
        // ID를 제외하고 엔티티 생성 (Builder 사용)
        KeyStatisticEntity keyStatistic = KeyStatisticEntity.builder()
                .stat_code("test code")
                .stat_name("테스트 통계") // stat_name은 nullable=false 이므로 필수
                .stat_value("100.5")
                .stat_cycle("M") // 월간 주기
                .stat_unit("퍼센트")
                .stat_date("20231027") // 날짜 형식에 맞게
                .build();

        KeyStatisticEntity savedEntity = keyStatisticRepository.save(keyStatistic);
    }
}