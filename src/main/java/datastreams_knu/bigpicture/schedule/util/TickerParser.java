package datastreams_knu.bigpicture.schedule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datastreams_knu.bigpicture.common.config.AiModelConfig;
import datastreams_knu.bigpicture.common.exception.ObjectMapperException;
import datastreams_knu.bigpicture.schedule.service.dto.RecommendedKeywordDto;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static datastreams_knu.bigpicture.schedule.util.TickerParserPrompt.TICKER_PARSER_PROMPT;

@RequiredArgsConstructor
@Service
public class TickerParser {

    private final AiModelConfig aiModelConfig;
    private final ObjectMapper objectMapper;

    private ChatLanguageModel model;

    @PostConstruct
    public void init() {
        this.model = aiModelConfig.openAiChatModel();
    }

    public RecommendedKeywordDto parseTicker(String ticker) {
        try {
            UserMessage prompt = createPrompt(ticker);

            String response = model.chat(prompt).aiMessage().text();

            return objectMapper.readValue(response, RecommendedKeywordDto.class);
        } catch (JsonProcessingException e) {
            throw new ObjectMapperException("Json 파싱 중 예외가 발생하였습니다.", e);
        }
    }

    private UserMessage createPrompt(String ticker) {
        String basePrompt = TICKER_PARSER_PROMPT;

        String prompt = basePrompt.formatted(ticker);

        UserMessage userMessage = new UserMessage(prompt);
        return userMessage;
    }
}
