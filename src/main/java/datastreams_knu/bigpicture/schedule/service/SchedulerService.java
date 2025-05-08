package datastreams_knu.bigpicture.schedule.service;

import datastreams_knu.bigpicture.common.dto.DateRangeDto;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import datastreams_knu.bigpicture.schedule.controller.dto.RegisterCrawlingDataResponse;
import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import datastreams_knu.bigpicture.schedule.entity.CrawlingSeed;
import datastreams_knu.bigpicture.schedule.repository.CrawlingInfoRepository;
import datastreams_knu.bigpicture.schedule.repository.CrawlingSeedRepository;
import datastreams_knu.bigpicture.schedule.service.dto.RecommendedKeywordDto;
import datastreams_knu.bigpicture.schedule.service.dto.RegisterCrawlingDataServiceRequest;
import datastreams_knu.bigpicture.schedule.util.TickerParser;
import datastreams_knu.bigpicture.stock.agent.dto.KoreaStockCrawlingDto;
import datastreams_knu.bigpicture.stock.agent.dto.USStockCrawlingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Value("${us-stock.api.base-url}")
    private String usStockBaseUrl;
    @Value("${us-stock.api.key}")
    private String usStockApiKey;

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

        // 실제 있는 stock인지 확인 필요
        if (isInvalidStockName(request)) {
            throw new IllegalArgumentException("유효하지 않은 stockName 입니다.");
        }

        CrawlingSeed crawlingSeed = CrawlingSeed.of(request.getStockType(), request.getStockName(), crawlingKeyword);
        crawlingSeedRepository.save(crawlingSeed);

        CrawlingInfo crawlingInfo = CrawlingInfo.of(request.getStockType(), request.getStockName(), crawlingKeyword);

        return RegisterCrawlingDataResponse.of(crawlingInfoRepository.save(crawlingInfo));
    }

    private boolean isInvalidStockName(RegisterCrawlingDataServiceRequest request) {
        return (request.getStockType().equals("korea") && !isExistentKoreaStock(request.getStockName()))
            || (request.getStockType().equals("us") && !isExistentUSStock(request.getStockName()));
    }

    public boolean isExistentKoreaStock(String stockName) {
        String encodedStockName = encodeString(stockName);
        DateRangeDto dateRange = getKoreaStockDateRange();
        String url = createKoreaStockUrl(encodedStockName, dateRange);

        KoreaStockCrawlingDto result = webClientUtil.get(url, KoreaStockCrawlingDto.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return result.getResponse().getBody().getItems().getItem().size() > 0;
    }

    public boolean isExistentUSStock(String stockName) {
        DateRangeDto dateRange = getUSStockDateRange();
        String url = createUSStockUrl(stockName, dateRange);

        USStockCrawlingDto response = webClientUtil.get(url, USStockCrawlingDto.class);

        return response.getResults() != null;
    }

    private String createKoreaStockUrl(String stockName, DateRangeDto dateRange) {
        StringBuilder sb = new StringBuilder();
        sb.append(koreaStockBaseUrl);
        sb.append("?serviceKey=")
            .append(koreaStockApiKey);
        sb.append("&itmsNm=")
            .append(stockName);
        sb.append("&beginBasDt=")
            .append(dateRange.getFromDate());
        sb.append("&endBasDt=")
            .append(dateRange.getToDate());
        sb.append("&resultType=json")
            .append("&numOfRows=1");

        return sb.toString();
    }

    private String createUSStockUrl(String stockName, DateRangeDto dateRange) {
        StringBuilder sb = new StringBuilder();
        sb.append(usStockBaseUrl);
        sb.append("ticker/").append(stockName);
        sb.append("/range/1/day")
            .append("/" + dateRange.getFromDate())
            .append("/" + dateRange.getToDate());
        sb.append("?apiKey=")
            .append(usStockApiKey);
        sb.append("&limit=1");
        return sb.toString();
    }

    private DateRangeDto getKoreaStockDateRange() {
        LocalDate now = LocalDate.now();
        LocalDate past = now.minusYears(YEARS_TO_SUBTRACT);
        String nowDate = parseDate(now);
        String pastDate = parseDate(past);
        return DateRangeDto.of(pastDate, nowDate);
    }

    private DateRangeDto getUSStockDateRange() {
        LocalDate now = LocalDate.now();
        LocalDate past = now.minusDays(YEARS_TO_SUBTRACT);
        String pastDate = past.toString();
        String nowDate = now.toString();
        return DateRangeDto.of(pastDate, nowDate);
    }

    private String parseDate(LocalDate date) {
        return date.toString().replace("-", "");
    }

    private String encodeString(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }
}
