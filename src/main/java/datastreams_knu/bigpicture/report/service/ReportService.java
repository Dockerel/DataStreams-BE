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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static datastreams_knu.bigpicture.report.util.ReportConst.EVALUATION_SCORE_THRESHOLD;
import static datastreams_knu.bigpicture.report.util.ReportConst.MAX_REPS;
import static datastreams_knu.bigpicture.report.util.ReportPrompt.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportService {

    private final ExchangeRepository exchangeRepository;
    private final KoreaInterestRepository koreaInterestRepository;
    private final USInterestRepository usInterestRepository;
    private final CrawlingInfoRepository crawlingInfoRepository;
    private final NewsRepository newsRepository;
    private final StockRepository stockRepository;

    private final ObjectMapper objectMapper;

    private final AiModelConfig aiModelConfig;

    private ChatLanguageModel model;

    @PostConstruct
    public void init() {
        this.model = aiModelConfig.openAiChatModel();
    }

    @Transactional(readOnly = true)
    public CreateReportResponse createReport(CreateReportServiceRequest request) {
        String fullReport = "";
        String summaryKeyword = "경제";
        String paramString = createParamString(request);
        // 1. 환율, 금리, 경제 뉴스 불러오기
        // 환율
        String exchangesString = getExchangeData();
        // 한국 금리
        String koreaInterestsString = getKoreaInterestData();
        // 미국 금리
        String usInterestsString = getUsInterestData();
        // 경제 뉴스
        String economyNewsString = getEconomyNewsData();
        // 2. (환율 + 금리 + 경제 뉴스)로 경제 레포트 생성 및 평가
        String economyReport = createAndEvaluateEconomyReport(exchangesString, koreaInterestsString, usInterestsString, economyNewsString, paramString);
        fullReport += economyReport + "\n\n";
        // : 경제 레포트 생성 요청이면 [6] 으로 이동
        String reportType = request.getReportType();
        String stockReport = "";
        String stockInfosString = "";
        if (reportType.equals("stock")) {
            String stockName = request.getStockName();
            summaryKeyword = stockName;
            // 3. 주가, 주식 뉴스 불러오기
            String keyword = getStockKeyword(stockName);
            String stockNewsString = getStockNews(keyword);
            stockInfosString = getStockInfosData(stockName);
            // 4. (경제 레포트 + 주가 + 주식 뉴스)로 주식 레포트 생성 및 평가
            stockReport = createAndEvaluateStockReport(stockReport, stockName, economyReport, stockInfosString, stockNewsString, paramString);
            // 5. 두 레포트(경제 레포트, 주식 레포트) 수합
            fullReport += stockReport + "\n\n";
        }
        // 6. (만들어진 레포트)로 요약 레포트 생성 및 평가
        String summaryReport = createAndEvaluateSummaryReport(summaryKeyword, fullReport, paramString);
        // 7. 요약 레포트로 제목 생성 및 평가
        String reportTitle = createAndEvaluateReportTitle(summaryReport, paramString);
        // 8. 제목, 요약 레포트, 경제 레포트, 주식 레포트 결과로 반환
        return CreateReportResponse.of(reportTitle, summaryReport, economyReport, stockReport, exchangesString, koreaInterestsString, usInterestsString, stockInfosString);
    }

    private String createAndEvaluateSummaryReport(String summaryKeyword, String fullReport, String paramString) {
        String summaryReport = "";
        String targetPrompt = summaryKeyword.equals("경제") ? SUMMARY_REPORT_GENERATION_PROMPT : SUMMARY_STOCK_REPORT_GENERATION_PROMPT;
        String summaryReportGenerationPrompt = String.format(targetPrompt, summaryKeyword, fullReport, paramString);
        UserMessage summaryReportGenerationUserMessage = new UserMessage(summaryReportGenerationPrompt);
        for (int i = 0; i < MAX_REPS; i++) {
            try {
                summaryReport = model.chat(summaryReportGenerationUserMessage).aiMessage().text();

                String summaryReportEvaluationPrompt = String.format(SUMMARY_REPORT_EVALUATION_PROMPT, summaryReport, fullReport, paramString);
                UserMessage summaryReportEvaluationUserMessage = new UserMessage(summaryReportEvaluationPrompt);
                String summaryReportEvaluationResultString = model.chat(summaryReportEvaluationUserMessage).aiMessage().text();

                ReportEvaluationResult summaryReportEvaluationResult = parseToJson(summaryReportEvaluationResultString, ReportEvaluationResult.class);
                int score = summaryReportEvaluationResult.getScore();
                if (score >= EVALUATION_SCORE_THRESHOLD) {
                    break;
                }
            } catch (Exception e) {
                i -= 1;
            }
        }
        return summaryReport;
    }

    private String createAndEvaluateStockReport(String stockReport, String stockName, String economyReport, String stockInfosString, String stockNewsString, String paramString) {
        stockReport = stockReport;
        String stockReportGenerationPrompt = String.format(STOCK_REPORT_GENERATION_PROMPT, stockName, economyReport, stockInfosString, stockNewsString, paramString);
        UserMessage stockReportGenerationUserMessage = new UserMessage(stockReportGenerationPrompt);
        for (int i = 0; i < MAX_REPS; i++) {
            try {
                stockReport = model.chat(stockReportGenerationUserMessage).aiMessage().text();

                String stockReportEvaluationPrompt = String.format(STOCK_REPORT_EVALUATION_PROMPT, stockReport, economyReport, stockInfosString, stockNewsString, paramString);
                UserMessage stockReportEvaluationUserMessage = new UserMessage(stockReportEvaluationPrompt);
                String stockReportEvaluationResultString = model.chat(stockReportEvaluationUserMessage).aiMessage().text();

                ReportEvaluationResult stockReportEvaluationResult = parseToJson(stockReportEvaluationResultString, ReportEvaluationResult.class);
                int score = stockReportEvaluationResult.getScore();
                if (score >= EVALUATION_SCORE_THRESHOLD) {
                    break;
                }
            } catch (Exception e) {
                i -= 1;
            }
        }
        return stockReport;
    }

    private String getStockInfosData(String stockName) {
        Stock stock = getStock(stockName);
        List<StockPromptInputDto> stockInfos = stock.getStockInfos().stream()
                .map(stockInfo -> StockPromptInputDto.from(stockInfo))
                .collect(Collectors.toList());
        String stockInfosData = parseToString(stockInfos);
        return stockInfosData;
    }

    private Stock getStock(String stockName) {
        Stock stock = stockRepository.findByStockName(stockName)
                .orElseThrow(IllegalArgumentException::new);
        return stock;
    }

    private String getStockNews(String keyword) {
        List<NewsPromptInputDto> newsList = newsRepository.findAllByKeyword(keyword).stream()
                .map(news -> NewsPromptInputDto.from(news))
                .collect(Collectors.toList());
        String newsListData = parseToString(newsList);
        return newsListData;
    }

    private String getStockKeyword(String stockName) {
        CrawlingInfo crawlingInfo = crawlingInfoRepository.findByStockName(stockName)
                .orElseThrow(IllegalArgumentException::new);
        String keyword = crawlingInfo.getStockKeyword();
        return keyword;
    }

    private String createAndEvaluateEconomyReport(String exchangesString, String koreaInterestsString, String usInterestsString, String economyNewsString, String paramString) {
        String economyReportGenerationPrompt = String.format(ECONOMY_REPORT_GENERATION_PROMPT, exchangesString, koreaInterestsString, usInterestsString, economyNewsString, paramString);
        UserMessage economyReportGenerationUserMessage = new UserMessage(economyReportGenerationPrompt);
        String economyReport = "";
        for (int i = 0; i < MAX_REPS; i++) {
            try {
                economyReport = model.chat(economyReportGenerationUserMessage).aiMessage().text();

                String economyReportEvaluationPrompt = String.format(ECONOMY_REPORT_EVALUATION_PROMPT, economyReport, exchangesString, koreaInterestsString, usInterestsString, economyNewsString, paramString);
                UserMessage economyReportEvaluationUserMessage = new UserMessage(economyReportEvaluationPrompt);
                String economyReportEvaluationResultString = model.chat(economyReportEvaluationUserMessage).aiMessage().text();

                ReportEvaluationResult economyReportEvaluationResult = parseToJson(economyReportEvaluationResultString, ReportEvaluationResult.class);
                int score = economyReportEvaluationResult.getScore();
                if (score >= EVALUATION_SCORE_THRESHOLD) {
                    break;
                }
            } catch (Exception e) {
                i -= 1;
            }
        }
        return economyReport;
    }

    private String getEconomyNewsData() {
        // 국내 경제 뉴스
        List<NewsPromptInputDto> koreaNewsList = newsRepository.findAllByKeyword("국내").stream()
                .map(news -> NewsPromptInputDto.from(news))
                .collect(Collectors.toList());
        String koreaNewsListString = parseToString(koreaNewsList);
        // 세계 경제 뉴스
        List<NewsPromptInputDto> worldNewsList = newsRepository.findAllByKeyword("해외").stream()
                .map(news -> NewsPromptInputDto.from(news))
                .collect(Collectors.toList());
        String worldNewsListString = parseToString(worldNewsList);
        // 국내 경제 뉴스 + 세계 경제 뉴스
        String economyNewsString = koreaNewsListString + worldNewsListString;
        return economyNewsString;
    }

    private String getUsInterestData() {
        List<InterestPromptInputDto> usInterests = usInterestRepository.findAll().stream()
                .map(interest -> InterestPromptInputDto.from(interest))
                .collect(Collectors.toList());
        String usInterestsString = parseToString(usInterests);
        return usInterestsString;
    }

    private String getKoreaInterestData() {
        List<InterestPromptInputDto> koreaInterests = koreaInterestRepository.findAll().stream()
                .map(interest -> InterestPromptInputDto.from(interest))
                .collect(Collectors.toList());
        String koreaInterestsString = parseToString(koreaInterests);
        return koreaInterestsString;
    }

    private String getExchangeData() {
        List<ExchangePromptInputDto> exchanges = exchangeRepository.findAll().stream()
                .map(exchange -> ExchangePromptInputDto.from(exchange))
                .collect(Collectors.toList());
        String exchangesString = parseToString(exchanges);
        return exchangesString;
    }

    private String createAndEvaluateReportTitle(String summaryReport, String paramString) {
        String reportTitle = "";
        String reportTitleGenerationPrompt = String.format(REPORT_TITLE_GENERATION_PROMPT, summaryReport, paramString);
        UserMessage reportTitleGenerationUserMessage = new UserMessage(reportTitleGenerationPrompt);
        for (int i = 0; i < MAX_REPS; i++) {
            try {
                reportTitle = model.chat(reportTitleGenerationUserMessage).aiMessage().text();

                String reportTitleEvaluationPrompt = String.format(REPORT_TITLE_EVALUATION_PROMPT, reportTitle, summaryReport, paramString);
                UserMessage reportTitleEvaluationUserMessage = new UserMessage(reportTitleEvaluationPrompt);
                String reportTitleEvaluationResultString = model.chat(reportTitleEvaluationUserMessage).aiMessage().text();

                ReportTitleEvaluationResult summaryReportEvaluationResult = parseToJson(reportTitleEvaluationResultString, ReportTitleEvaluationResult.class);
                int score = summaryReportEvaluationResult.getScore();
                if (score >= EVALUATION_SCORE_THRESHOLD) {
                    break;
                }
            } catch (Exception e) {
                i -= 1;
            }
        }
        return reportTitle;
    }

    private String createParamString(CreateReportServiceRequest params) {
        return """
                [레포트의 성향]
                위험 수용 성향 : %s
                레포트 난이도 : %s
                관심 분야 : %s
                """.formatted(params.getRiskTolerance(), params.getReportDifficultyLevel(), params.getInterestAreas(), parseToString(params.getInterestAreas()));
    }

    private String parseToString(Object object) {
        int retryCount = MAX_REPS;
        while (true) {
            try {
                String result = objectMapper.writeValueAsString(object);
                return result;
            } catch (JsonProcessingException e) {
                retryCount--;
                if (retryCount == 0) {
                    throw new ObjectMapperException("직렬화 중 예외가 발생하였습니다.", e);
                }
            }
        }
    }

    private <T> T parseToJson(String jsonString, Class<T> clazz) {
        int retryCount = MAX_REPS;
        while (true) {
            try {
                return objectMapper.readValue(jsonString, clazz);
            } catch (JsonProcessingException e) {
                retryCount--;
                if (retryCount == 0) {
                    throw new ObjectMapperException("Json 파싱 중 예외가 발생하였습니다.", e);
                }
            }
        }
    }
}
