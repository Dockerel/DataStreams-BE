package datastreams_knu.bigpicture.schedule.scheduler;

import datastreams_knu.bigpicture.schedule.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GlobalScheduler {

    private final SchedulerService schedulerService;

    // 달러 환율 크롤링
    @Scheduled(cron = "0 0 1 * * MON", zone = "Asia/Seoul") // 월요일 새벽 1시
    public void runExchangeCrawling() {
        schedulerService.exchangeCrawling();
    }

    // 한국 / 미국 금리 크롤링
    @Scheduled(cron = "0 30 1 * * MON", zone = "Asia/Seoul") // 월요일 새벽 1시 30분
    public void runInterestCrawling() {
        schedulerService.interestCrawling();
    }

    // 기본 경제 뉴스 크롤링
    @Scheduled(cron = "0 0 2 * * MON", zone = "Asia/Seoul") // 월요일 새벽 2시
    public void runGeneralNewsCrawling() {
        schedulerService.generalNewsCrawling();
    }

    // 주가 + 관련 뉴스 크롤링
    @Scheduled(cron = "0 30 2 * * MON", zone = "Asia/Seoul") // 월요일 새벽 2시 30분
    public void runNewsCrawling() {
        schedulerService.stockAndNewsCrawling();
    }
}
