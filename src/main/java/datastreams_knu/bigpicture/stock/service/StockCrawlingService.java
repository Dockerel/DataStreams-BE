package datastreams_knu.bigpicture.stock.service;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import datastreams_knu.bigpicture.common.dto.DateRangeDto;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import datastreams_knu.bigpicture.stock.agent.StockCrawlingAssistant;
import datastreams_knu.bigpicture.stock.agent.dto.KoreaStockCrawlingDto;
import datastreams_knu.bigpicture.stock.agent.dto.StockInfoDto;
import datastreams_knu.bigpicture.stock.agent.dto.USStockCrawlingDto;
import datastreams_knu.bigpicture.stock.config.StockCrawlingConfig;
import datastreams_knu.bigpicture.stock.entity.Stock;
import datastreams_knu.bigpicture.stock.entity.StockInfo;
import datastreams_knu.bigpicture.stock.entity.StockType;
import datastreams_knu.bigpicture.stock.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StockCrawlingService {

    public static final int MONTHS_TO_SUBTRACT = 1;
    private final WebClientUtil webClientUtil;
    private final StockRepository stockRepository;

    @Value("${korea-stock.api.base-url}")
    private String koreaStockBaseUrl;
    @Value("${korea-stock.api.key}")
    private String koreaStockApiKey;

    @Value("${us-stock.api.base-url}")
    private String usStockBaseUrl;
    @Value("${us-stock.api.key}")
    private String usStockApiKey;

    private final StockCrawlingConfig stockCrawlingConfig;
    private StockCrawlingAssistant stockCrawlingAssistant;

    @PostConstruct
    public void init() {
        stockCrawlingAssistant = stockCrawlingConfig.stockCrawlingAssistant();
    }

    @Transactional
    public CrawlingResultDto crawling(String type, String stockName) {
        return stockCrawlingAssistant.execute(type, stockName);
    }

    @Transactional
    public CrawlingResultDto dataInit(String stockName, String stockType) {
        List<StockInfoDto> stockInfos = getStockInfos(stockType, stockName);

        Stock stock = Stock.of(stockName, getStockType(stockType));

        stockInfos.stream()
            .map(info -> StockInfo.of(info.getStockPrice(), info.getStockDate()))
            .forEach(stock::addStockInfo);

        stockRepository.save(stock);

        return CrawlingResultDto.of(true, "주가 크롤링 성공");
    }

    private List<StockInfoDto> getStockInfos(String stockType, String stockKeyword) {
        List<StockInfoDto> stockInfos;
        if (stockType.equals("korea")) {
            String encodedStockName = encodeString(stockKeyword);
            DateRangeDto dateRange = getKoreaStockDateRange();
            String url = createKoreaStockUrl(encodedStockName, dateRange);

            KoreaStockCrawlingDto result = webClientUtil.get(url, KoreaStockCrawlingDto.class);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            stockInfos = result.getResponse().getBody().getItems().getItem().stream()
                .map(item -> {
                    LocalDate stockDate = LocalDate.parse(item.getBasDt(), formatter);
                    double stockPrice = Double.parseDouble(item.getClpr());
                    return StockInfoDto.of(stockDate, stockPrice);
                })
                .sorted(Comparator.comparing(StockInfoDto::getStockDate))
                .collect(Collectors.toList());
        } else {
            DateRangeDto dateRange = getUSStockDateRange();
            String url = createUSStockUrl(stockKeyword, dateRange);

            USStockCrawlingDto response = webClientUtil.get(url, USStockCrawlingDto.class);

            stockInfos = response.getResults().stream()
                .map(result -> {
                    Instant stockInstant = Instant.ofEpochMilli(result.getT());
                    LocalDate stockDate = stockInstant.atZone(ZoneId.of("Asia/Seoul")).toLocalDate();
                    return StockInfoDto.of(stockDate, result.getC());
                })
                .collect(Collectors.toList());
        }
        return stockInfos;
    }

    private StockType getStockType(String type) {
        return type.equals("korea") ? StockType.KOREA : StockType.US;
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
            .append("&numOfRows=50");

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
        return sb.toString();
    }

    private DateRangeDto getKoreaStockDateRange() {
        LocalDate now = LocalDate.now();
        LocalDate past = now.minusMonths(MONTHS_TO_SUBTRACT);
        String nowDate = parseDate(now);
        String pastDate = parseDate(past);
        return DateRangeDto.of(pastDate, nowDate);
    }

    private DateRangeDto getUSStockDateRange() {
        LocalDate now = LocalDate.now();
        LocalDate past = now.minusMonths(MONTHS_TO_SUBTRACT);
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
