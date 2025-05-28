package datastreams_knu.bigpicture.news.config;

import datastreams_knu.bigpicture.common.config.AiModelConfig;
import datastreams_knu.bigpicture.news.agent.NewsCrawlingAgent;
import datastreams_knu.bigpicture.news.agent.NewsCrawlingAssistant;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class NewsCrawlingConfig {

    private final AiModelConfig aiModelConfig;
    private final NewsCrawlingAgent newsCrawlingAgent;

    @Bean
    public NewsCrawlingAssistant newsCrawlingAssistant() {
        return AiServices.builder(NewsCrawlingAssistant.class)
                .chatLanguageModel(aiModelConfig.openAiChatModel())
                .tools(newsCrawlingAgent)
                .build();
    }
}
