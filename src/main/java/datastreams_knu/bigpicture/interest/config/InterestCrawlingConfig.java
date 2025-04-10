package datastreams_knu.bigpicture.interest.config;

import datastreams_knu.bigpicture.common.config.AiModelConfig;
import datastreams_knu.bigpicture.interest.agent.InterestCrawlingAgent;
import datastreams_knu.bigpicture.interest.agent.InterestCrawlingAssistant;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class InterestCrawlingConfig {

    private final AiModelConfig aiModelConfig;
    private final InterestCrawlingAgent interestCrawlingAgent;

    @Bean
    public InterestCrawlingAssistant interestCrawlingAssistant() {
        return AiServices.builder(InterestCrawlingAssistant.class)
            .chatLanguageModel(aiModelConfig.openAiChatModel())
            .tools(interestCrawlingAgent)
            .build();
    }
}
