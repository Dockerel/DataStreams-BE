package datastreams_knu.bigpicture.interest.service;

import datastreams_knu.bigpicture.interest.agent.InterestCrawlingAssistant;
import datastreams_knu.bigpicture.interest.agent.dto.InterestCrawlingResultDto;
import datastreams_knu.bigpicture.interest.config.InterestCrawlingConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class InterestCrawlingService {

    private final InterestCrawlingConfig interestCrawlingConfig;
    private final InterestCrawlingAssistant interestCrawlingAssistant;

    public InterestCrawlingService(InterestCrawlingConfig interestCrawlingConfig,
                                   InterestCrawlingAssistant interestCrawlingAssistant) {
        this.interestCrawlingConfig = interestCrawlingConfig;
        this.interestCrawlingAssistant = interestCrawlingAssistant;
    }

    public InterestCrawlingResultDto crawling(String type, int n) {
        return interestCrawlingAssistant.execute(type, n);
    }
}
