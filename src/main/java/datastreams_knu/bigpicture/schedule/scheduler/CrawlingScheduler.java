package datastreams_knu.bigpicture.schedule.scheduler;

import datastreams_knu.bigpicture.schedule.service.CrawlingSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CrawlingScheduler {

    private final CrawlingSchedulerService crawlingSchedulerService;

    // 달러 환율 크롤링
    @Scheduled(cron = "0 10 23 * * SUN", zone = "Asia/Seoul") // 매주 일요일 오후 11시 10분
    public void runExchangeCrawling() {
        crawlingSchedulerService.exchangeCrawling();
    }

    // 한국 / 미국 금리 크롤링
    @Scheduled(cron = "0 0 1 20 * *", zone = "Asia/Seoul") // 매달 20일 오전 1시
    public void runInterestCrawling() {
        crawlingSchedulerService.interestCrawling();
    }

    // 기본 경제 뉴스 크롤링
    @Scheduled(cron = "0 50 1 * * *", zone = "Asia/Seoul") // 매일 오전 1시 50분
    public void runGeneralNewsCrawling() {
        crawlingSchedulerService.generalNewsCrawling();
    }

    // 키워드 뉴스 크롤링
    @Scheduled(cron = "0 0 2 * * *", zone = "Asia/Seoul") // 매일 오전 2시
    public void runNewsCrawling() {
        crawlingSchedulerService.newsCrawling();
    }

    // 한국 주식 주가 크롤링
    @Scheduled(cron = "0 0 13 * * *", zone = "Asia/Seoul") // 매일 오후 1시
    public void runKoreaStockCrawling() {
        crawlingSchedulerService.koreaStockCrawling();
    }

    // 미국 주식 주가 크롤링
    @Scheduled(cron = "0 0 14 * * *", zone = "Asia/Seoul") // 매일 오후 2시
    public void runUSStockCrawling() {
        crawlingSchedulerService.usStockCrawling();
    }

    // 새로운 키워드 크롤링(뉴스 + 주가)
    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul") // 매일 오전 4시
    public void runCrawlingSeedCrawling() {
        crawlingSchedulerService.crawlingSeedCrawling();
    }
}
