package datastreams_knu.bigpicture.stock.service;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import datastreams_knu.bigpicture.stock.agent.StockCrawlingAssistant;
import datastreams_knu.bigpicture.stock.config.StockCrawlingConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StockCrawlingService {

    private final StockCrawlingConfig stockCrawlingConfig;
    private StockCrawlingAssistant stockCrawlingAssistant;

    @PostConstruct
    public void init() {
        stockCrawlingAssistant = stockCrawlingConfig.stockCrawlingAssistant();
    }

    public CrawlingResultDto crawling(String type, String stockName) {
        return stockCrawlingAssistant.execute(type, stockName);
    }
}
