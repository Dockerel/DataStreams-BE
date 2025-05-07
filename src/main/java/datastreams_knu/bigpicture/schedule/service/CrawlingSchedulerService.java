package datastreams_knu.bigpicture.schedule.service;

import datastreams_knu.bigpicture.exchange.service.ExchangeCrawlingService;
import datastreams_knu.bigpicture.interest.service.InterestCrawlingService;
import datastreams_knu.bigpicture.news.service.NewsCrawlingService;
import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import datastreams_knu.bigpicture.schedule.entity.CrawlingSeed;
import datastreams_knu.bigpicture.schedule.repository.CrawlingInfoRepository;
import datastreams_knu.bigpicture.schedule.repository.CrawlingSeedRepository;
import datastreams_knu.bigpicture.schedule.util.RetryExecutor;
import datastreams_knu.bigpicture.stock.service.StockCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final CrawlingSeedRepository crawlingSeedRepository;

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
            if (info.getStockKeyword().equals("unknown")) continue;
            String newsCrawlingTaskName = "NewsCrawling/%s-%s".formatted("keyword", info.getStockKeyword());
            RetryExecutor.execute(() -> newsCrawlingService.crawling("keyword", info.getStockKeyword()), newsCrawlingTaskName);
        }
    }

    // 한국 주식 주가
    public void koreaStockCrawling() {
        List<CrawlingInfo> crawlingInfos = crawlingInfoRepository.findAllByStockType("korea");
        for (CrawlingInfo info : crawlingInfos) {
            String stockCrawlingTaskName = "StockCrawling-Korea/%s-%s-%s".formatted(info.getStockType(), info.getStockName(), info.getStockKeyword());
            RetryExecutor.execute(() -> stockCrawlingService.crawling(info.getStockType(), info.getStockKeyword()), stockCrawlingTaskName);
        }
    }

    // 미국 주식 주가
    public void usStockCrawling() {
        List<CrawlingInfo> crawlingInfos = crawlingInfoRepository.findAllByStockType("us");
        for (CrawlingInfo info : crawlingInfos) {
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

    // 새로운 키워드 (주가 + 뉴스)
    public void crawlingSeedCrawling() {
        List<CrawlingSeed> koreaCrawlingSeeds = crawlingSeedRepository.findAllByStockType("korea");
        List<CrawlingSeed> usCrawlingSeeds = crawlingSeedRepository.findAllByStockType("us");

        int minLength = Math.min(koreaCrawlingSeeds.size(), usCrawlingSeeds.size());

        List<CrawlingSeed> crawlingSeeds = new ArrayList<>();
        for (int i = 0; i < minLength; i++) {
            crawlingSeeds.add(usCrawlingSeeds.get(i));
            crawlingSeeds.add(koreaCrawlingSeeds.get(i));
        }

        for (int i = minLength; i < koreaCrawlingSeeds.size(); i++) {
            crawlingSeeds.add(koreaCrawlingSeeds.get(i));
        }
        for (int i = minLength; i < usCrawlingSeeds.size(); i++) {
            crawlingSeeds.add(usCrawlingSeeds.get(i));
        }

        String prevStockType = null;
        for (CrawlingSeed crawlingSeed : crawlingSeeds) {
            // api 호출 제한을 피하기 위한 대기 시간(60초)
            // 연속으로 us가 2번 호출된 경우 대기
            try {
                if (prevStockType != null && prevStockType.equals("us") && crawlingSeed.getStockType().equals("us")) {
                    Thread.sleep(60 * 1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 주가 : 1달 동안의 주가 데이터 수집 필요
            if (crawlingSeed.getStockType().equals("korea")) {
                String stockCrawlingTaskName = "StockCrawling-Korea/%s-%s-%s".formatted(crawlingSeed.getStockType(), crawlingSeed.getStockName(), crawlingSeed.getStockKeyword());
                RetryExecutor.execute(() -> stockCrawlingService.dataInit(crawlingSeed.getStockName(),crawlingSeed.getStockType(), crawlingSeed.getStockKeyword()), stockCrawlingTaskName);
            } else {
                String stockCrawlingTaskName = "StockCrawling-US/%s-%s-%s".formatted(crawlingSeed.getStockType(), crawlingSeed.getStockName(), crawlingSeed.getStockKeyword());
                RetryExecutor.execute(() -> stockCrawlingService.dataInit(crawlingSeed.getStockName(),crawlingSeed.getStockType(), crawlingSeed.getStockKeyword()), stockCrawlingTaskName);
            }

            // 뉴스 : 일주일 동안의 뉴스 데이터 수집 필요
            if (!crawlingSeed.getStockKeyword().equals("unknown")) {
                String newsCrawlingTaskName = "NewsCrawling/%s-%s".formatted("keyword", crawlingSeed.getStockKeyword());
                RetryExecutor.execute(() -> newsCrawlingService.dataInit(crawlingSeed.getStockKeyword()), newsCrawlingTaskName);
            }

            prevStockType = crawlingSeed.getStockType();

            crawlingSeedRepository.delete(crawlingSeed);
        }
    }
}
