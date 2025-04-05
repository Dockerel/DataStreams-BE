package datastreams_knu.bigpicture.stock.config;

import datastreams_knu.bigpicture.common.config.AiModelConfig;
import datastreams_knu.bigpicture.interest.agent.InterestCrawlingAgent;
import datastreams_knu.bigpicture.interest.agent.InterestCrawlingAssistant;
import datastreams_knu.bigpicture.stock.agent.StockCrawlingAgent;
import datastreams_knu.bigpicture.stock.agent.StockCrawlingAssistant;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class StockCrawlingConfig {

    private final AiModelConfig aiModelConfig;
    private final StockCrawlingAgent stockCrawlingAgent;

    @Bean
    public StockCrawlingAssistant stockCrawlingAssistant() {
        return AiServices.builder(StockCrawlingAssistant.class)
            .chatLanguageModel(aiModelConfig.openAiChatModel())
            .tools(stockCrawlingAgent)
            .build();
    }
}
