package datastreams_knu.bigpicture.schedule.service;

import datastreams_knu.bigpicture.exchange.service.ExchangeCrawlingService;
import datastreams_knu.bigpicture.interest.service.InterestCrawlingService;
import datastreams_knu.bigpicture.news.service.NewsCrawlingService;
import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import datastreams_knu.bigpicture.schedule.repository.CrawlingInfoRepository;
import datastreams_knu.bigpicture.schedule.util.RetryExecutor;
import datastreams_knu.bigpicture.stock.service.StockCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrawlingSchedulerService {

    public static final int INTEREST_DATA_TARGET_MONTH = 1;

    private final ExchangeCrawlingService exchangeCrawlingService;
    private final InterestCrawlingService interestCrawlingService;
    private final StockCrawlingService stockCrawlingService;
    private final NewsCrawlingService newsCrawlingService;

    private final CrawlingInfoRepository crawlingInfoRepository;

    public void exchangeCrawling() {
        RetryExecutor.execute(() -> exchangeCrawlingService.crawling(), "ExchangeCrawlingService");
    }

    public void interestCrawling() {
        RetryExecutor.execute(() -> interestCrawlingService.crawling("korea", INTEREST_DATA_TARGET_MONTH), "InterestCrawlingService/korea");
        RetryExecutor.execute(() -> interestCrawlingService.crawling("us", INTEREST_DATA_TARGET_MONTH), "InterestCrawlingService/us");
    }

    // 기본 경제 뉴스
    public void generalNewsCrawling() {
        RetryExecutor.execute(() -> newsCrawlingService.crawling("general", "국내"), "NewsCrawling/general-국내");
        RetryExecutor.execute(() -> newsCrawlingService.crawling("general", "해외"), "NewsCrawling/general-해외");
    }

    // 키워드 뉴스
    public void newsCrawling() {
        List<CrawlingInfo> crawlingInfos = crawlingInfoRepository.findAll();
        for (CrawlingInfo info : crawlingInfos) {
            String newsCrawlingTaskName = "NewsCrawling/%s-%s".formatted("keyword", info.getStockKeyword());
            RetryExecutor.execute(() -> newsCrawlingService.crawling("keyword", info.getStockKeyword()), newsCrawlingTaskName);
        }
    }

    // 한국 주식 주가
    public void koreaStockCrawling() {
        List<CrawlingInfo> crawlingInfos = crawlingInfoRepository.findAllByStockType("korea");
        for (CrawlingInfo info : crawlingInfos) {
            // 뉴스 크롤링 키워드가 존재하지 않는 경우 패스
            if (info.getStockName().equals("unknown")) continue;

            String stockCrawlingTaskName = "StockCrawling-Korea/%s-%s-%s".formatted(info.getStockType(), info.getStockName(), info.getStockKeyword());
            RetryExecutor.execute(() -> stockCrawlingService.crawling(info.getStockType(), info.getStockKeyword()), stockCrawlingTaskName);
        }
    }

    // 미국 주식 주가
    public void usStockCrawling() {
        List<CrawlingInfo> crawlingInfos = crawlingInfoRepository.findAllByStockType("us");
        for (CrawlingInfo info : crawlingInfos) {
            // 뉴스 크롤링 키워드가 존재하지 않는 경우 패스
            if (info.getStockName().equals("unknown")) continue;

            // api 호출 제한을 피하기 위한 대기 시간(60초)
            try {
                String stockCrawlingTaskName = "StockCrawling-US/%s-%s-%s".formatted(info.getStockType(), info.getStockName(), info.getStockKeyword());
                RetryExecutor.execute(() -> stockCrawlingService.crawling(info.getStockType(), info.getStockKeyword()), stockCrawlingTaskName);
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
