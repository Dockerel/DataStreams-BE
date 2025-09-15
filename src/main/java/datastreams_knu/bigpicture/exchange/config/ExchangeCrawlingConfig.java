package datastreams_knu.bigpicture.exchange.config;

import datastreams_knu.bigpicture.common.config.AiModelConfig;
import datastreams_knu.bigpicture.exchange.agent.ExchangeCrawlingAgent;
import datastreams_knu.bigpicture.exchange.agent.ExchangeCrawlingAssistant;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ExchangeCrawlingConfig {

    private final AiModelConfig aiModelConfig;
    private final ExchangeCrawlingAgent exchangeCrawlingAgent;

    @Bean
    public ExchangeCrawlingAssistant exchangeCrawlingAssistant() {
        return AiServices.builder(ExchangeCrawlingAssistant.class)
                .chatLanguageModel(aiModelConfig.geminiChatModel())
                .tools(exchangeCrawlingAgent)
                .build();
    }
}
