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

    @Scheduled(cron = "0 0 6-23 * * *", zone = "Asia/Seoul") // 매 시간 (6시 ~ 23시)
    public void sendNewsAlert() {
        newsAlertService.sendNewsAlerts(LocalDateTime.now());
    }
}
