package datastreams_knu.bigpicture.exchange.service;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import datastreams_knu.bigpicture.exchange.agent.ExchangeCrawlingAssistant;
import datastreams_knu.bigpicture.exchange.config.ExchangeCrawlingConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExchangeCrawlingService {

    private final ExchangeCrawlingConfig exchangeCrawlingConfig;
    private ExchangeCrawlingAssistant exchangeCrawlingAssistant;

    @PostConstruct
    public void init() {
        exchangeCrawlingAssistant = exchangeCrawlingConfig.exchangeCrawlingAssistant();
    }

    public CrawlingResultDto crawling() {
        return exchangeCrawlingAssistant.execute();
    }
}
