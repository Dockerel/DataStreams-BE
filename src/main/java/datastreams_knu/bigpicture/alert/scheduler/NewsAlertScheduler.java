package datastreams_knu.bigpicture.alert.scheduler;

import datastreams_knu.bigpicture.alert.service.NewsAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class NewsAlertScheduler {

    private final NewsAlertService newsAlertService;

    @Scheduled(cron = "0 0 6-23 * * *", zone = "Asia/Seoul") // 매시간
    public void sendNewsAlert() {
        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).minusHours(1);
        newsAlertService.sendNewsAlerts(now);
    }
}
