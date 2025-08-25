package datastreams_knu.bigpicture.study;

import datastreams_knu.bigpicture.alert.service.NewsAlertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;

@SpringBootTest
public class Study {

    @Autowired
    NewsAlertService newsAlertService;

    @Test
    void test() throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime oneHourBeforeLocalDateTime = localDateTime.withMinute(0).withSecond(0).withNano(0).minusHours(1);

        newsAlertService.crawlingNews("삼성전자", oneHourBeforeLocalDateTime);
    }
}
