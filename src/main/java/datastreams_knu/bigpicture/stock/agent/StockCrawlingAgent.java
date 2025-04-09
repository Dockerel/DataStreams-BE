package datastreams_knu.bigpicture.stock.agent;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import datastreams_knu.bigpicture.common.dto.DateRangeDto;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import datastreams_knu.bigpicture.stock.agent.dto.KoreaStockCrawlingDto;
import datastreams_knu.bigpicture.stock.agent.dto.StockInfoDto;
import datastreams_knu.bigpicture.stock.agent.dto.USStockCrawlingDto;
import datastreams_knu.bigpicture.stock.entity.Stock;
import datastreams_knu.bigpicture.stock.entity.StockInfo;
import datastreams_knu.bigpicture.stock.entity.StockType;
import datastreams_knu.bigpicture.stock.repository.StockInfoRepository;
import datastreams_knu.bigpicture.stock.repository.StockRepository;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
@Component
public class StockCrawlingAgent {

    private final WebClientUtil webClientUtil;
    private final StockRepository stockRepository;
    private final StockInfoRepository stockInfoRepository;

    @Value("${korea-stock.api.base-url}")
    private String koreaStockBaseUrl;
    @Value("${korea-stock.api.key}")
    private String koreaStockApiKey;

    @Value("${us-stock.api.base-url}")
    private String usStockBaseUrl;
    @Value("${us-stock.api.key}")
    private String usStockApiKey;

    @Tool("주어진 'stockName'에 해당하는 한국 주식의 주가 데이터를 수집합니다.")
    public List<StockInfoDto> crawlingKoreaStockByName(String stockName) {
        String encodedStockName = encodeString(stockName);
        DateRangeDto dateRange = getKoreaStockDateRange();
        String url = createKoreaStockUrl(encodedStockName, dateRange);

        KoreaStockCrawlingDto result = webClientUtil.get(url, KoreaStockCrawlingDto.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return result.getResponse().getBody().getItems().getItem().stream()
            .map(item -> {
                LocalDate stockDate = LocalDate.parse(item.getBasDt(), formatter);
                double stockPrice = Double.parseDouble(item.getClpr());
                return StockInfoDto.of(stockDate, stockPrice);
            })
            .sorted(Comparator.comparing(StockInfoDto::getStockDate))
            .collect(Collectors.toList());
    }

    @Tool("주어진 'stockName'에 해당하는 미국 주식의 주가 데이터를 수집합니다.")
    public List<StockInfoDto> crawlingUSStockByTicker(String stockName) {
        DateRangeDto dateRange = getUSStockDateRange();
        String url = createUSStockUrl(stockName, dateRange);

        USStockCrawlingDto response = webClientUtil.get(url, USStockCrawlingDto.class);

        return response.getResults().stream()
            .map(result -> {
                Instant stockInstant = Instant.ofEpochMilli(result.getT());
                LocalDate stockDate = stockInstant.atZone(ZoneId.of("Asia/Seoul")).toLocalDate();
                return StockInfoDto.of(stockDate, result.getC());
            })
            .collect(Collectors.toList());
    }

    @Tool("크롤링 된 주가 데이터를 주식의 'type'과 'stockName'과 함께 DB에 저장하고 처리 성공 여부를 반환합니다.")
    public CrawlingResultDto saveStock(String type, String stockName, List<StockInfoDto> stockInfos) {
        Stock stock = stockRepository.findByName(stockName)
            .orElse(Stock.of(stockName, getStockType(type)));

        stockInfoRepository.deleteAllByStockId(stock.getId());

        stockInfos.stream()
            .map(info -> StockInfo.of(info.getStockPrice(), info.getStockDate()))
            .forEach(stock::addStockInfo);

        stockRepository.save(stock);

        return CrawlingResultDto.of(true, "주가 크롤링 성공");
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
        LocalDate past = now.minusMonths(1);
        String nowDate = parseDate(now);
        String pastDate = parseDate(past);
        return DateRangeDto.of(pastDate, nowDate);
    }

    private DateRangeDto getUSStockDateRange() {
        LocalDate now = LocalDate.now();
        LocalDate past = now.minusMonths(1);
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
