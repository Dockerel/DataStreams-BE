package datastreams_knu.bigpicture.interest.service;

import datastreams_knu.bigpicture.interest.agent.InterestCrawlingAssistant;
import datastreams_knu.bigpicture.interest.agent.dto.InterestCrawlingResultDto;
import datastreams_knu.bigpicture.interest.config.InterestCrawlingConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class InterestCrawlingService {

    private final InterestCrawlingConfig interestCrawlingConfig;
    private InterestCrawlingAssistant interestCrawlingAssistant;

    @PostConstruct
    public void init() {
        interestCrawlingAssistant = interestCrawlingConfig.interestCrawlingAssistant();
    }

    public InterestCrawlingResultDto crawling(String type, int n) {
        return interestCrawlingAssistant.execute(type, n);
    }
}
