package datastreams_knu.bigpicture.schedule.service;

import datastreams_knu.bigpicture.common.util.StockNameValidator;
import datastreams_knu.bigpicture.common.util.TickerParser;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import datastreams_knu.bigpicture.schedule.controller.dto.RegisterCrawlingDataResponse;
import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import datastreams_knu.bigpicture.schedule.entity.CrawlingSeed;
import datastreams_knu.bigpicture.schedule.repository.CrawlingInfoRepository;
import datastreams_knu.bigpicture.schedule.repository.CrawlingSeedRepository;
import datastreams_knu.bigpicture.schedule.service.dto.RegisterCrawlingDataServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class SchedulerService {

    public static final int YEARS_TO_SUBTRACT = 1;
    private final WebClientUtil webClientUtil;

    @Value("${korea-stock.api.base-url}")
    private String koreaStockBaseUrl;
    @Value("${korea-stock.api.key}")
    private String koreaStockApiKey;

    @Value("${python.server.url}")
    private String pythonServerUrl;

    private final CrawlingInfoRepository crawlingInfoRepository;
    private final CrawlingSeedRepository crawlingSeedRepository;

    private final StockNameValidator stockNameValidator;
    private final TickerParser tickerParser;

    public RegisterCrawlingDataResponse registerCrawlingData(RegisterCrawlingDataServiceRequest request) {
        // 이미 존재하는 크롤링 정보
        Optional<CrawlingInfo> findCrawlingInfo = crawlingInfoRepository.findByStockName(request.getStockName());
        if (findCrawlingInfo.isPresent()) {
            return RegisterCrawlingDataResponse.from(findCrawlingInfo.get());
        }

        String crawlingKeyword = request.getStockName();
        if (request.getStockType().equals("us")) {
            crawlingKeyword = tickerParser.parseTicker(request.getStockName());
        }

        // 실제 있는 stock인지 확인 필요
        if (stockNameValidator.isInvalidStockName(request.getStockName(), request.getStockType())) {
            throw new IllegalArgumentException("유효하지 않은 stockName 입니다.");
        }

        CrawlingSeed crawlingSeed = CrawlingSeed.of(request.getStockType(), request.getStockName(), crawlingKeyword);

        return RegisterCrawlingDataResponse.from(crawlingSeedRepository.save(crawlingSeed));
    }
}
