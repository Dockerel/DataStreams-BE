package datastreams_knu.bigpicture.common.util;

import datastreams_knu.bigpicture.common.dto.DateRangeDto;
import datastreams_knu.bigpicture.common.dto.UsStockCheckDto;
import datastreams_knu.bigpicture.stock.agent.dto.KoreaStockCrawlingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class StockNameValidator {

    public static final int YEARS_TO_SUBTRACT = 1;

    private final WebClientUtil webClientUtil;

    @Value("${korea-stock.api.base-url}")
    private String koreaStockBaseUrl;
    @Value("${korea-stock.api.key}")
    private String koreaStockApiKey;

    @Value("${python.server.url}")
    private String pythonServerUrl;

    public boolean isInvalidStockName(String stockName, String stockType) {
        return (stockType.equals("korea") && !isExistentKoreaStock(stockName))
                || (stockType.equals("us") && !isExistentUSStock(stockName));
    }

    private boolean isExistentKoreaStock(String stockName) {
        String encodedStockName = encodeString(stockName);
        DateRangeDto dateRange = getKoreaStockDateRange();
        String url = createKoreaStockUrl(encodedStockName, dateRange);

        KoreaStockCrawlingDto result = webClientUtil.get(url, KoreaStockCrawlingDto.class);

        return result.getResponse().getBody().getItems().getItem().size() > 0;
    }

    public boolean isExistentUSStock(String stockName) {
        String url = pythonServerUrl + "/api/v1/stocks/check/" + stockName;

        UsStockCheckDto response = webClientUtil.get(url, UsStockCheckDto.class);

        return response.getData();
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

    private DateRangeDto getKoreaStockDateRange() {
        LocalDate now = LocalDate.now();
        LocalDate past = now.minusYears(YEARS_TO_SUBTRACT);
        String nowDate = parseDate(now);
        String pastDate = parseDate(past);
        return DateRangeDto.of(pastDate, nowDate);
    }

    private String parseDate(LocalDate date) {
        return date.toString().replace("-", "");
    }

    private String encodeString(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }
}
