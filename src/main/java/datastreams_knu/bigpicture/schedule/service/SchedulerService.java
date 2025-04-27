package datastreams_knu.bigpicture.schedule.service;

import datastreams_knu.bigpicture.schedule.controller.dto.RegisterCrawlingDataResponse;
import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import datastreams_knu.bigpicture.schedule.repository.CrawlingInfoRepository;
import datastreams_knu.bigpicture.schedule.service.dto.RecommendedKeywordDto;
import datastreams_knu.bigpicture.schedule.service.dto.RegisterCrawlingDataServiceRequest;
import datastreams_knu.bigpicture.schedule.util.TickerParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SchedulerService {

    private final CrawlingInfoRepository crawlingInfoRepository;
    private final TickerParser tickerParser;

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
