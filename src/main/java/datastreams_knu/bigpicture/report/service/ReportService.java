package datastreams_knu.bigpicture.report.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datastreams_knu.bigpicture.common.config.AiModelConfig;
import datastreams_knu.bigpicture.common.exception.ObjectMapperException;
import datastreams_knu.bigpicture.exchange.repository.ExchangeRepository;
import datastreams_knu.bigpicture.interest.repository.KoreaInterestRepository;
import datastreams_knu.bigpicture.interest.repository.USInterestRepository;
import datastreams_knu.bigpicture.news.repository.NewsRepository;
import datastreams_knu.bigpicture.report.service.dto.*;
import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import datastreams_knu.bigpicture.schedule.repository.CrawlingInfoRepository;
import datastreams_knu.bigpicture.stock.entity.Stock;
import datastreams_knu.bigpicture.stock.repository.StockRepository;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static datastreams_knu.bigpicture.report.util.ReportPrompt.ECONOMY_REPORT_GENERATION_PROMPT;
import static datastreams_knu.bigpicture.report.util.ReportPrompt.STOCK_REPORT_GENERATION_PROMPT;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReportService {

    private final ExchangeRepository exchangeRepository;
    private final KoreaInterestRepository koreaInterestRepository;
    private final USInterestRepository usInterestRepository;
    private final NewsRepository newsRepository;
    private final StockRepository stockRepository;
    private final CrawlingInfoRepository crawlingInfoRepository;

    private final AiModelConfig aiModelConfig;
    private final ObjectMapper objectMapper;

    private ChatLanguageModel model;

    @PostConstruct
    public void init() {
        this.model = aiModelConfig.geminiChatModel();
    }

    /**
     * param reportType : "economy", "stock"
     * param stockName  : if reportType=="stock" then use "stockName"
     *
     * @return
     */
    @Transactional
    public ReportDto createReport(CreateReportServiceRequest request) {
        /**
         * 1. 환율
         * 2. 금리 (한국, 미국)
         * 3. 뉴스 (stockName or 국내/해외)
         * 4. 주가
         * -> 경제 레포트 vs 주식 + 경제 레포트
         */
        String reportType = request.getReportType();

        String reportInputText = """
            1. 환율 데이터
            %s

            2. 한국 금리 데이터
            %s

            3. 미국 금리 데이터
            %s

            4. 뉴스 데이터
            %s
            """;

        String stockInputText = """

            5. 주가 데이터
            %s
            """;

        List<ExchangePromptInputDto> exchanges = exchangeRepository.findAll().stream()
            .map(exchange -> ExchangePromptInputDto.from(exchange))
            .collect(Collectors.toList());
        String exchangesData = parseToString(exchanges);

        List<InterestPromptInputDto> koreaInterests = koreaInterestRepository.findAll().stream()
            .map(interest -> InterestPromptInputDto.from(interest))
            .collect(Collectors.toList());
        String koreaInterestsData = parseToString(koreaInterests);

        List<InterestPromptInputDto> usInterests = usInterestRepository.findAll().stream()
            .map(interest -> InterestPromptInputDto.from(interest))
            .collect(Collectors.toList());
        String usInterestsData = parseToString(usInterests);

        String newsData = "";
        if (reportType.equals("economy")) {
            List<NewsPromptInputDto> koreaNewsList = newsRepository.findAllByKeyword("국내").stream()
                .map(news -> NewsPromptInputDto.from(news))
                .collect(Collectors.toList());
            String koreaNewsListData = parseToString(koreaNewsList);

            List<NewsPromptInputDto> worldNewsList = newsRepository.findAllByKeyword("해외").stream()
                .map(news -> NewsPromptInputDto.from(news))
                .collect(Collectors.toList());
            String worldNewsListData = parseToString(worldNewsList);

            newsData = koreaNewsListData + worldNewsListData;
        }
        if (reportType.equals("stock")) {
            String stockName = request.getStockName();
            CrawlingInfo crawlingInfo = crawlingInfoRepository.findByStockName(stockName)
                .orElseThrow(IllegalArgumentException::new);
            String keyword = crawlingInfo.getStockKeyword();

            List<NewsPromptInputDto> newsList = newsRepository.findAllByKeyword(keyword).stream()
                .map(news -> NewsPromptInputDto.from(news))
                .collect(Collectors.toList());
            String newsListData = parseToString(newsList);

            Stock stock = stockRepository.findByStockName(stockName)
                .orElseThrow(IllegalArgumentException::new);
            List<StockPromptInputDto> stockInfos = stock.getStockInfos().stream()
                .map(stockInfo -> StockPromptInputDto.from(stockInfo))
                .collect(Collectors.toList());
            String stockInfosData = parseToString(stockInfos);

            newsData = newsListData;

            stockInputText = stockInputText.formatted(stockInfosData);
        }

        reportInputText = reportInputText.formatted(exchangesData, koreaInterestsData, usInterestsData, newsData);

        String fullInputTest = reportInputText + (reportType.equals("stock") ? stockInputText : "");

        String fullPrompt = reportType.equals("economy") ? ECONOMY_REPORT_GENERATION_PROMPT.formatted(request.getRiskTolerance(), request.getReportDifficultyLevel(), parseToString(request.getInterestAreas()), fullInputTest)
            : STOCK_REPORT_GENERATION_PROMPT.formatted(request.getRiskTolerance(), request.getReportDifficultyLevel(), parseToString(request.getInterestAreas()), fullInputTest);

        UserMessage prompt = new UserMessage(fullPrompt);
        String response = model.chat(prompt).aiMessage().text();

        String processedResponse = response.replace("```json", "").replace("```", "");

        try {
            ReportDto report = objectMapper.readValue(processedResponse, ReportDto.class);
            return report;
        } catch (JsonProcessingException e) {
            throw new ObjectMapperException("Json 파싱 중 예외가 발생하였습니다.", e);
        }
    }

    public String parseToString(Object object) {
        int retryCount = 3;
        while (true) {
            try {
                String result = objectMapper.writeValueAsString(object);
                return result;
            } catch (JsonProcessingException e) {
                retryCount--;
                if (retryCount == 0) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
