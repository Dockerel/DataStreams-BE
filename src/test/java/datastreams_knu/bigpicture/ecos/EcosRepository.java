package datastreams_knu.bigpicture.ecos;

import datastreams_knu.bigpicture.ecos.domain.KeyStatisticEntity;
import datastreams_knu.bigpicture.ecos.repository.KeyStatisticRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@DataJpaTest
public class EcosRepository {

    @Autowired
    KeyStatisticRepository keyStatisticRepository;

    @Test
    void test() {
        KeyStatisticEntity keyStatistic = new KeyStatisticEntity("code", "name", "stat_value", "cycle", "unit", LocalDate.now());
        keyStatisticRepository.save(keyStatistic);
    }
}
