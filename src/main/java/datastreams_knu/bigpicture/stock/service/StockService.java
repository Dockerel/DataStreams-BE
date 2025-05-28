package datastreams_knu.bigpicture.stock.service;

import datastreams_knu.bigpicture.common.util.StockKeywordResolver;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import datastreams_knu.bigpicture.stock.controller.dto.StockResponse;
import datastreams_knu.bigpicture.stock.service.dto.CheckTickerResponseDto;
import datastreams_knu.bigpicture.stock.service.dto.StockInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static datastreams_knu.bigpicture.stock.service.dto.StockInfoResponseDto.Data;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StockService {

    public static final int MONTHS_TO_SUBTRACT = 1;
    private final StockKeywordResolver stockKeywordResolver;
    private final WebClientUtil webClientUtil;

    @Value("${python.server.url}")
    private String pythonServerUrl;

    public List<StockResponse> getStocks(String stockName) {
        String code = stockKeywordResolver.resolve(stockName);
        String url = pythonServerUrl + "/api/v1/stocks/" + code + getDateRange();
        StockInfoResponseDto response = webClientUtil.get(url, StockInfoResponseDto.class);
        return response.getData().stream()
                .map(data -> {
                    LocalDate date = LocalDate.parse(data.getDate().split("T")[0]);
                    return StockResponse.of(date, data.getClosePrice());
                })
                .collect(Collectors.toList());
    }

    public StockResponse getTodayStockInfo(String stockName) {
        String code = stockKeywordResolver.resolve(stockName);
        String url = pythonServerUrl + "/api/v1/stocks/" + code;
        StockInfoResponseDto response = webClientUtil.get(url, StockInfoResponseDto.class);
        Data data = response.getData().get(0);
        LocalDate date = LocalDate.parse(data.getDate().split("T")[0]);
        return StockResponse.of(date, data.getClosePrice());
    }

    public Boolean checkTicker(String ticker) {
        String url = pythonServerUrl + "/api/v1/stocks/check/" + ticker;
        CheckTickerResponseDto response = webClientUtil.get(url, CheckTickerResponseDto.class);
        return response.getIsValidTicker();
    }

    private static String getDateRange() {
        LocalDate now = LocalDate.now();
        String monthsAgoLocalDate = now.minusMonths(MONTHS_TO_SUBTRACT).toString();
        String todayLocalDate = now.toString();
        return "?fromDate=" + monthsAgoLocalDate + "&toDate=" + todayLocalDate;
    }
}
