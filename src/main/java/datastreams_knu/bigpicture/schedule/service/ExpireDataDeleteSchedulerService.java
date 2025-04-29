package datastreams_knu.bigpicture.schedule.service;

import datastreams_knu.bigpicture.common.dto.DeleteResultDto;
import datastreams_knu.bigpicture.exchange.repository.ExchangeRepository;
import datastreams_knu.bigpicture.interest.repository.KoreaInterestRepository;
import datastreams_knu.bigpicture.interest.repository.USInterestRepository;
import datastreams_knu.bigpicture.news.repository.NewsRepository;
import datastreams_knu.bigpicture.stock.repository.StockInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional
@RequiredArgsConstructor
@Service
public class ExpireDataDeleteSchedulerService {

    public static final int EXCHANGE_DATA_EXPIRE_MONTH = 3;
    public static final int STOCK_DATA_EXPIRE_MONTH = 1;
    public static final int INTEREST_DATA_EXPIRE_YEAR = 1;
    public static final int NEWS_DATA_EXPIRE_DAY = 7;

    private final ExchangeRepository exchangeRepository;
    private final KoreaInterestRepository koreaInterestRepository;
    private final USInterestRepository usInterestRepository;
    private final NewsRepository newsRepository;
    private final StockInfoRepository stockInfoRepository;

    public DeleteResultDto deleteExpireExchangeData() {
        int deleteCount = exchangeRepository.deleteAllByExchangeDateBefore(LocalDate.now().minusMonths(EXCHANGE_DATA_EXPIRE_MONTH));
        return DeleteResultDto.of(deleteCount, "환율 데이터 삭제 성공");
    }

    public DeleteResultDto deleteExpireKoreaInterestData() {
        int deleteCount = koreaInterestRepository.deleteAllByInterestDateBefore(LocalDate.now().minusYears(INTEREST_DATA_EXPIRE_YEAR));
        return DeleteResultDto.of(deleteCount, "한국 금리 데이터 삭제 성공");
    }

    public DeleteResultDto deleteExpireUSInterestData() {
        int deleteCount = usInterestRepository.deleteAllByInterestDateBefore(LocalDate.now().minusYears(INTEREST_DATA_EXPIRE_YEAR));
        return DeleteResultDto.of(deleteCount, "미국 금리 데이터 삭제 성공");
    }

    public DeleteResultDto deleteExpireNewsData() {
        int deleteCount = newsRepository.deleteAllByNewsCrawlingDateBefore(LocalDate.now().minusDays(NEWS_DATA_EXPIRE_DAY));
        return DeleteResultDto.of(deleteCount, "뉴스 데이터 삭제 성공");
    }

    public DeleteResultDto deleteExpireStockData() {
        int deleteCount = stockInfoRepository.deleteAllByStockDateBefore(LocalDate.now().minusMonths(STOCK_DATA_EXPIRE_MONTH));
        return DeleteResultDto.of(deleteCount, "주가 데이터 삭제 성공");
    }
}
