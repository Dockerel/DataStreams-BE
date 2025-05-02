package datastreams_knu.bigpicture.common.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

@Configuration
public class AiModelConfig {

    @Value("${openai.api.key}")
    public String OPENAI_API_KEY;

    @Bean
    public ChatLanguageModel openAiChatModel() {
        return OpenAiChatModel.builder()
            .apiKey(OPENAI_API_KEY)
            .modelName(GPT_4_O_MINI)
            .timeout(Duration.ofSeconds(120))
            .strictTools(true)
            .build();
    }
}
