package datastreams_knu.bigpicture.stock.service;

import datastreams_knu.bigpicture.stock.controller.dto.StockResponse;
import datastreams_knu.bigpicture.stock.entity.Stock;
import datastreams_knu.bigpicture.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;

    public List<StockResponse> getStocks(String stockName) {
        Stock stock = stockRepository.findByStockName(stockName)
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 stockName 입니다."));

        return stock.getStockInfos().stream()
            .map(stockInfo -> StockResponse.from(stockInfo))
            .collect(Collectors.toList());
    }
}
