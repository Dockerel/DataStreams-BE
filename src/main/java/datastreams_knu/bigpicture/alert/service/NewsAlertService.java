package datastreams_knu.bigpicture.alert.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datastreams_knu.bigpicture.alert.entity.Watchlist;
import datastreams_knu.bigpicture.alert.repository.WatchlistRepository;
import datastreams_knu.bigpicture.alert.service.dto.AlertNewsResponse;
import datastreams_knu.bigpicture.alert.service.dto.CrawledNewsDto;
import datastreams_knu.bigpicture.common.config.AiModelConfig;
import datastreams_knu.bigpicture.common.dto.DateRangeDto;
import datastreams_knu.bigpicture.common.exception.ObjectMapperException;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static datastreams_knu.bigpicture.alert.service.dto.CrawledNewsDto.Result;

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

    // todo : filter 기준 하나 더 넣어서 개선, notnull 검사, 감성 분석(진짜 관련 있는 뉴스인지 llm으로 판단)
    public List<AlertNewsResponse> crawlingNews(String keyword, LocalDateTime targetLocalDateTime) {
        Map<String, String> params = Map.of("&query=", URLEncoder.encode(keyword, StandardCharsets.UTF_8));

        String url = createUrl(params);

        CrawledNewsDto response = webClientUtil.get(url, CrawledNewsDto.class);

        List<Result> crawledNewsResults = response.getYIB_KR_A().getResult();

        return crawledNewsResults.stream()
                .filter(result -> {
                    String dateStr = result.getDATETIME();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
                    return dateTime.isAfter(targetLocalDateTime);
                })
                .map(result -> AlertNewsResponse.from(result))
                .collect(Collectors.toList());
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
