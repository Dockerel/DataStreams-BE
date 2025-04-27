package datastreams_knu.bigpicture.schedule.service;

import datastreams_knu.bigpicture.exchange.service.ExchangeCrawlingService;
import datastreams_knu.bigpicture.interest.service.InterestCrawlingService;
import datastreams_knu.bigpicture.news.service.NewsCrawlingService;
import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import datastreams_knu.bigpicture.schedule.repository.CrawlingInfoRepository;
import datastreams_knu.bigpicture.schedule.util.RetryExecutor;
import datastreams_knu.bigpicture.schedule.util.TickerParser;
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
    private final TickerParser tickerParser;

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

    // 주가 + 관련 뉴스
    public void stockAndNewsCrawling() {
        List<CrawlingInfo> crawlingInfos = crawlingInfoRepository.findAll();
        for (CrawlingInfo info : crawlingInfos) {
            String newsCrawlingTaskName = "NewsCrawling/%s-%s".formatted("keyword", info.getNewsKeyword());
            RetryExecutor.execute(() -> newsCrawlingService.crawling("keyword", info.getNewsKeyword()), newsCrawlingTaskName);

            // 뉴스 크롤링 키워드가 존재하지 않는 경우 패스
            if (info.getNewsKeyword().equals("unknown")) continue;

            String stockCrawlingTaskName = "StockCrawling/%s-%s".formatted(info.getStockType(), info.getStockName());
            RetryExecutor.execute(() -> stockCrawlingService.crawling(info.getStockType(), info.getStockName()), stockCrawlingTaskName);
        }
    }
}
