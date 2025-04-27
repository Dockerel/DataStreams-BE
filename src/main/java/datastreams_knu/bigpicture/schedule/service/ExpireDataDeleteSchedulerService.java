package datastreams_knu.bigpicture.schedule.service;

import datastreams_knu.bigpicture.common.dto.DeleteResultDto;
import datastreams_knu.bigpicture.exchange.repository.ExchangeRepository;
import datastreams_knu.bigpicture.exchange.service.ExchangeCrawlingService;
import datastreams_knu.bigpicture.interest.repository.KoreaInterestRepository;
import datastreams_knu.bigpicture.interest.repository.USInterestRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExpireDataDeleteSchedulerService {

    public static final int EXCHANGE_DATA_EXPIRE_MONTH = 3;
    public static final int INTEREST_DATA_EXPIRE_YEAR = 1;

    private final ExchangeRepository exchangeRepository;
    private final KoreaInterestRepository koreaInterestRepository;
    private final USInterestRepository usInterestRepository;

    @Transactional
    public DeleteResultDto deleteExpireExchangeData() {
        int deleteCount = exchangeRepository.deleteAllByExchangeDateBefore(LocalDate.now().minusMonths(EXCHANGE_DATA_EXPIRE_MONTH));
        return DeleteResultDto.of(deleteCount, "환율 데이터 삭제 성공");
    }

    @Transactional
    public DeleteResultDto deleteExpireKoreaInterestData() {
        int deleteCount = koreaInterestRepository.deleteAllByInterestDateBefore(LocalDate.now().minusYears(INTEREST_DATA_EXPIRE_YEAR));
        return DeleteResultDto.of(deleteCount, "한국 금리 데이터 삭제 성공");
    }

    @Transactional
    public DeleteResultDto deleteExpireUSInterestData() {
        int deleteCount = usInterestRepository.deleteAllByInterestDateBefore(LocalDate.now().minusYears(INTEREST_DATA_EXPIRE_YEAR));
        return DeleteResultDto.of(deleteCount, "미국 금리 데이터 삭제 성공");
    }
}
