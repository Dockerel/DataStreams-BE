package datastreams_knu.bigpicture.news.service;

import datastreams_knu.bigpicture.news.agent.NewsCrawlingAssistant;
import datastreams_knu.bigpicture.news.agent.dto.NewsCrawlingResultDto;
import datastreams_knu.bigpicture.news.config.NewsCrawlingConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class NewsCrawlingService {

    private final NewsCrawlingConfig newsCrawlingConfig;
    private final NewsCrawlingAssistant newsCrawlingAssistant;

    public NewsCrawlingService(NewsCrawlingConfig newsCrawlingConfig,
                               NewsCrawlingAssistant newsCrawlingAssistant) {
        this.newsCrawlingConfig = newsCrawlingConfig;
        this.newsCrawlingAssistant = newsCrawlingConfig.newsCrawlingAssistant();
    }

    public NewsCrawlingResultDto crawling(String type, String keyword) {
        return newsCrawlingAssistant.execute(type, keyword);
    }
}
