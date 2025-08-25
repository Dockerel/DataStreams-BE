package datastreams_knu.bigpicture.alert.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datastreams_knu.bigpicture.alert.entity.Watchlist;
import datastreams_knu.bigpicture.alert.repository.WatchlistRepository;
import datastreams_knu.bigpicture.alert.service.dto.AlertNewsResponse;
import datastreams_knu.bigpicture.alert.service.dto.AnalysisNewsSentimentDto;
import datastreams_knu.bigpicture.alert.service.dto.CrawledNewsDto;
import datastreams_knu.bigpicture.common.config.AiModelConfig;
import datastreams_knu.bigpicture.common.dto.DateRangeDto;
import datastreams_knu.bigpicture.common.exception.ObjectMapperException;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import datastreams_knu.bigpicture.news.exception.NewsCrawlingException;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static datastreams_knu.bigpicture.alert.service.dto.CrawledNewsDto.Result;
import static datastreams_knu.bigpicture.alert.util.AlertPrompt.NEWS_SENTIMENT_ANALYSIS_PROMPT;

@RequiredArgsConstructor
@Service
public class NewsAlertService {

    public final WebClientUtil webClientUtil;

    public final FcmService fcmService;

    public final WatchlistRepository watchlistRepository;

    private final AiModelConfig aiModelConfig;
    private final ObjectMapper objectMapper;

    private ChatLanguageModel model;

    @PostConstruct
    public void init() {
        this.model = aiModelConfig.openAiChatModel();
    }

    public void sendNewsAlerts(LocalDateTime localDateTime) {
        LocalDateTime oneHourBeforeLocalDateTime = localDateTime.withMinute(0).withSecond(0).withNano(0).minusHours(1);
        watchlistRepository.findAll().stream()
                .forEach(watchlist -> sendNewsAlert(watchlist, oneHourBeforeLocalDateTime));
    }

    private void sendNewsAlert(Watchlist watchlist, LocalDateTime localDateTime) {
        List<AlertNewsResponse> alertNewsResponses = crawlingNews(watchlist.getStockKeyword(), localDateTime);
        alertNewsResponses.stream()
                .forEach(alertNewsResponse -> {
                    String newsMessage = createNewsMessageString(alertNewsResponse);
                    watchlist.getMemberWatchlists().stream()
                            .forEach(memberWatchlist -> {
                                String fcmToken = memberWatchlist.getMember().getFcmToken();
                                fcmService.sendMessageTo(fcmToken, "News Notification", newsMessage);
                            });
                });
    }

    private String createNewsMessageString(AlertNewsResponse alertNewsResponse) {
        try {
            return objectMapper.writeValueAsString(alertNewsResponse);
        } catch (JsonProcessingException e) {
            throw new ObjectMapperException("직렬화 중 예외가 발생하였습니다.", e);
        }
    }

    public List<AlertNewsResponse> crawlingNews(String keyword, LocalDateTime targetLocalDateTime) {
        Map<String, String> params = Map.of("&query=", URLEncoder.encode(keyword, StandardCharsets.UTF_8));

        String url = createUrl(params);

        CrawledNewsDto response = webClientUtil.get(url, CrawledNewsDto.class);

        List<Result> crawledNewsResults = response.getYIB_KR_A().getResult();

        return crawledNewsResults.stream()
                .filter(Objects::nonNull)
                .filter(result -> findRecentNews(targetLocalDateTime, result))
                .filter(result -> analysisNewsSentiment(result))
                .map(result -> AlertNewsResponse.from(result))
                .collect(Collectors.toList());
    }

    private static boolean findRecentNews(LocalDateTime targetLocalDateTime, Result result) {
        String dateStr = result.getDATETIME();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
        return dateTime.isAfter(targetLocalDateTime);
    }

    private boolean analysisNewsSentiment(Result result) {
        try {
            String content = getNewsContent(result);
            AnalysisNewsSentimentDto analysisNewsSentimentDto = getNewsSentiment(content);
            return !analysisNewsSentimentDto.label().equals("neutral");

        } catch (IOException e) {
            throw new NewsCrawlingException("뉴스 크롤링 중 오류가 발생하였습니다: " + e.getMessage());
        }
    }

    private AnalysisNewsSentimentDto getNewsSentiment(String content) throws JsonProcessingException {
        String promptText = NEWS_SENTIMENT_ANALYSIS_PROMPT;

        UserMessage prompt = createPrompt(promptText, content);

        String response = model.chat(prompt).aiMessage().text();

        AnalysisNewsSentimentDto analysisNewsSentimentDto = objectMapper.readValue(response, AnalysisNewsSentimentDto.class);
        return analysisNewsSentimentDto;
    }

    private static @NotNull String getNewsContent(Result result) throws IOException {
        String baseUrl = "https://www.yna.co.kr/view/";
        String url = baseUrl + result.getCID();

        Document doc = Jsoup.connect(url).get();
        Elements contentElements = doc.select(".story-news p");

        String content = contentElements.stream()
                .map(Element::text)
                .map(String::trim)
                .filter(text -> !text.isEmpty())
                .collect(Collectors.joining(" "));
        return content;
    }

    private UserMessage createPrompt(String promptText, String var) {
        String prompt = promptText.formatted(var);
        UserMessage userMessage = new UserMessage(prompt);
        return userMessage;
    }

    private static String createUrl(Map<String, String> params) {
        String baseUrl = "https://ars.yna.co.kr/api/v2/search.basic"
                + "?page_no=1&page_size=50&scope=all&sort=date&channel=basic_kr&cattr=A";

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(baseUrl);

        for (String s : params.keySet()) {
            stringBuilder.append(s);
            stringBuilder.append(params.get(s));
        }

        DateRangeDto range = getDateRange();
        stringBuilder
                .append("&from=")
                .append(range.getFromDate())
                .append("&to=")
                .append(range.getToDate());

        return stringBuilder.toString();
    }

    private static DateRangeDto getDateRange() {
        LocalDate today = LocalDate.now();
        return DateRangeDto.of(processDate(today));
    }

    private static String processDate(LocalDate date) {
        return date.toString().replace("-", "");
    }

}
