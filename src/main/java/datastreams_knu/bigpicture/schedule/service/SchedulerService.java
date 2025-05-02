package datastreams_knu.bigpicture.schedule.service;

import datastreams_knu.bigpicture.schedule.controller.dto.RegisterCrawlingDataResponse;
import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import datastreams_knu.bigpicture.schedule.entity.CrawlingSeed;
import datastreams_knu.bigpicture.schedule.repository.CrawlingInfoRepository;
import datastreams_knu.bigpicture.schedule.repository.CrawlingSeedRepository;
import datastreams_knu.bigpicture.schedule.service.dto.RecommendedKeywordDto;
import datastreams_knu.bigpicture.schedule.service.dto.RegisterCrawlingDataServiceRequest;
import datastreams_knu.bigpicture.schedule.util.TickerParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class SchedulerService {

    private final CrawlingInfoRepository crawlingInfoRepository;
    private final CrawlingSeedRepository crawlingSeedRepository;

    private final TickerParser tickerParser;

    public RegisterCrawlingDataResponse registerCrawlingData(RegisterCrawlingDataServiceRequest request) {
        // 이미 존재하는 크롤링 정보
        Optional<CrawlingInfo> findCrawlingInfo = crawlingInfoRepository.findByStockName(request.getStockName());
        if (findCrawlingInfo.isPresent()) {
            return RegisterCrawlingDataResponse.of(findCrawlingInfo.get());
        }

        String crawlingKeyword = request.getStockName();
        if (request.getStockType().equals("us")) {
            RecommendedKeywordDto response = tickerParser.parseTicker(request.getStockName());
            crawlingKeyword = response.getKeyword();
        }

        CrawlingSeed crawlingSeed = CrawlingSeed.of(request.getStockType(), request.getStockName(), crawlingKeyword);
        crawlingSeedRepository.save(crawlingSeed);

        CrawlingInfo crawlingInfo = CrawlingInfo.of(request.getStockType(), request.getStockName(), crawlingKeyword);

        return RegisterCrawlingDataResponse.of(crawlingInfoRepository.save(crawlingInfo));
    }
}
