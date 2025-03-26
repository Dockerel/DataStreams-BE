package datastreams_knu.bigpicture.news.service;

import datastreams_knu.bigpicture.news.agent.NewsCrawlingAssistant;
import datastreams_knu.bigpicture.news.config.NewsCrawlingConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NewsCrawlingService {

    private final NewsCrawlingConfig newsCrawlingConfig;

    private NewsCrawlingAssistant assistant;

    @PostConstruct
    public void setup() {
        this.assistant = newsCrawlingConfig.assistant();
    }

    @Transactional
    public void crawling(String type, String keyword) {
        assistant.execute(type, keyword);
    }
}
