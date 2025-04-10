package datastreams_knu.bigpicture.schedule.service;

import datastreams_knu.bigpicture.exchange.service.ExchangeCrawlingService;
import datastreams_knu.bigpicture.interest.service.InterestCrawlingService;
import datastreams_knu.bigpicture.news.service.NewsCrawlingService;
import datastreams_knu.bigpicture.schedule.controller.dto.RegisterCrawlingDataResponse;
import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import datastreams_knu.bigpicture.schedule.repository.CrawlingInfoRepository;
import datastreams_knu.bigpicture.schedule.service.dto.RecommendedKeywordDto;
import datastreams_knu.bigpicture.schedule.service.dto.RegisterCrawlingDataServiceRequest;
import datastreams_knu.bigpicture.schedule.util.RetryExecutor;
import datastreams_knu.bigpicture.stock.service.StockCrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulerService {

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
        RetryExecutor.execute(() -> interestCrawlingService.crawling("korea", 1), "InterestCrawlingService/korea");
        RetryExecutor.execute(() -> interestCrawlingService.crawling("us", 1), "InterestCrawlingService/us");
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

    @Transactional
    public RegisterCrawlingDataResponse registerCrawlingData(RegisterCrawlingDataServiceRequest request) {
        // 이미 존재하는 크롤링 정보
        Optional<CrawlingInfo> findCrawlingInfo = crawlingInfoRepository.findByStockName(request.getStockName());
        if (findCrawlingInfo.isPresent()) {
            return RegisterCrawlingDataResponse.of(findCrawlingInfo.get());
        }

        // 한국 기업의 경우 주식 이름을 뉴스 크롤링 키워드로 사용
        if (request.getStockType().equals("korea")) {
            String stockName = request.getStockName();
            return RegisterCrawlingDataResponse.of(CrawlingInfo.of(request.getStockType(), stockName, stockName));
        }

        // 해외 기업의 경우 LLM을 통해 ticker(stockName)로부터 적절한 키워드를 생성하여 뉴스 크롤링에 사용
        RecommendedKeywordDto response = tickerParser.parseTicker(request.getStockName());
        CrawlingInfo crawlingInfo = CrawlingInfo.of(request.getStockType(), request.getStockName(), response.getKeyword());
        return RegisterCrawlingDataResponse.of(crawlingInfoRepository.save(crawlingInfo));
    }
}
