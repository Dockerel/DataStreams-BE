package datastreams_knu.bigpicture.news.service;

import datastreams_knu.bigpicture.news.agent.NewsCrawlingAssistant;
import datastreams_knu.bigpicture.news.agent.dto.NewsCrawlingResultDto;
import datastreams_knu.bigpicture.news.config.NewsCrawlingConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NewsCrawlingService {

    private final NewsCrawlingConfig newsCrawlingConfig;
    private NewsCrawlingAssistant newsCrawlingAssistant;

    @PostConstruct
    public void init() {
        newsCrawlingAssistant = newsCrawlingConfig.newsCrawlingAssistant();
    }

    public NewsCrawlingResultDto crawling(String type, String keyword) {
        return newsCrawlingAssistant.execute(type, keyword);
    }
}
