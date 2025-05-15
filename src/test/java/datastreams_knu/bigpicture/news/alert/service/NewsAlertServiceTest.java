package datastreams_knu.bigpicture.news.alert.service;

import datastreams_knu.bigpicture.alert.service.NewsAlertService;
import datastreams_knu.bigpicture.alert.service.dto.AlertNewsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class NewsAlertServiceTest {

    @Autowired
    NewsAlertService newsAlertService;

    @Test
    void test() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime onTheHour = now.withHour(16).withMinute(0).withSecond(0).withNano(0);

        List<AlertNewsResponse> response = newsAlertService.crawlingNews("엔비디아", onTheHour);

        for (AlertNewsResponse r : response) {
            System.out.println("r = " + r);
            System.out.println("====================================");
        }
    }

}