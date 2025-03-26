package datastreams_knu.bigpicture.news.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datastreams_knu.bigpicture.common.config.AiModelConfig;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import datastreams_knu.bigpicture.news.agent.dto.*;
import datastreams_knu.bigpicture.news.domain.News;
import datastreams_knu.bigpicture.news.domain.NewsInfo;
import datastreams_knu.bigpicture.news.exception.NewsCrawlingException;
import datastreams_knu.bigpicture.news.repository.NewsRepository;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static datastreams_knu.bigpicture.news.agent.AgentPrompt.*;
import static datastreams_knu.bigpicture.news.agent.dto.CrawledNewsDto.YibKrA;

@RequiredArgsConstructor
@Slf4j
@Component
public class NewsCrawlingAgent {

    private final WebClientUtil webClientUtil;
    private final AiModelConfig aiModelConfig;
    private final ObjectMapper objectMapper;

    private final NewsRepository newsRepository;

    private ChatLanguageModel model;

    @PostConstruct
    private void setup() {
        this.model = aiModelConfig.openAiChatModel();
    }

    @Tool("keyword를 기반으로 지난 7일 동안의 관련 뉴스 기사를 검색합니다.")
    public List<SummarizedNewsDto> crawlingNewsByKeyword(String keyword) {
        Map<String, String> params = Map.of("&query=", keyword);
        String url = createUrl(params);

        CrawledNewsDto response = webClientUtil.get(url, CrawledNewsDto.class);

        List<String> newsIds = getNewsIds(response);

        List<SummarizedNewsDto> summarizedNewsList = newsIds.stream()
            .map(id -> getSummarizedNews(keyword, id))
            .collect(Collectors.toList());

        return summarizedNewsList;
    }

    @Tool("특정 키워드 없이 지난 7일 동안의 주요 경제 뉴스를 수집합니다.")
    public List<SummarizedNewsDto> crawlingNewsGeneral(String keyword) { // keyword : 국내, 해외
        String code = keyword.equals("국내") ? "02" : "11";
        Map<String, String> params = Map.of("&div_code=", code);
        String url = createUrl(params);

        CrawledNewsDto response = webClientUtil.get(url, CrawledNewsDto.class);

        List<String> newsIds = getNewsIds(response);

        List<SummarizedNewsDto> summarizedNewsList = newsIds.stream()
            .map(id -> getSummarizedNews(keyword, id))
            .collect(Collectors.toList());

        return summarizedNewsList;
    }

    @Tool("수집된 기사들을 바탕으로 기사 요약 정보와 기사 출처 URL을 생성합니다")
    public SummarizedMultipleNewsDto summarizeMultipleNews(List<SummarizedNewsDto> newsList) {
        try {
            String promptText = SUMMARIZE_MULTIPLE_NEWS_PROMPT;

            String var = createVar(newsList);

            UserMessage prompt = createPrompt(promptText, var);

            String response = model.chat(prompt).aiMessage().text();

            SummarizedMultipleNewsDto result = objectMapper.readValue(response, SummarizedMultipleNewsDto.class);

            return result;
        } catch (Exception e) {
            throw new NewsCrawlingException("뉴스들의 요약본 생성 중 오류가 발생하였습니다: " + e.getMessage());
        }
    }

    @Tool("생성된 기사 요약과 기사 출처 URL들 그리고 해당 기사의 날짜들을 DB에 저장하고 처리 성공 여부를 반환합니다.")
    public NewsCrawlingResultDto saveSummarizedNewsWithUrls(String keyword, SummarizedMultipleNewsDto summarizedMultipleNews) {
        News news = updateNews(keyword, summarizedMultipleNews);
        newsRepository.save(news);
        return NewsCrawlingResultDto.of(true, "성공");
    }

    private static String createUrl(Map<String, String> params) {
        // 뉴스 기사 최대 20개 크롤링 (연합뉴스)
        String baseUrl = "https://ars.yna.co.kr/api/v2/search.basic"
            + "?page_no=1&page_size=20&scope=all&sort=weight&channel=basic_kr&cattr=A";

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(baseUrl);

        for (String s : params.keySet()) {
            stringBuilder.append(s);
            stringBuilder.append(params.get(s));
        }

        DateRangeDto range = getDateRange("week");
        stringBuilder
            .append("&from=")
            .append(range.getFromDate())
            .append("&to=")
            .append(range.getToDate());

        return stringBuilder.toString();
    }

    private static DateRangeDto getDateRange(String option) {
        LocalDate today = LocalDate.now();

        if (option.equals("day")) {
            String processDateToday = processDate(today);
            return DateRangeDto.of(processDateToday);
        }

        // 이번 주 월요일 찾기
        LocalDate thisMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        // 저번 주 월요일 = 이번 주 월요일 - 7일
        LocalDate lastMonday = thisMonday.minusDays(7);
        // 저번 주 일요일 = 저번 주 월요일 + 6일
        LocalDate lastSunday = lastMonday.plusDays(6);

        return DateRangeDto.of(processDate(lastMonday), processDate(lastSunday));
    }

    private static String processDate(LocalDate date) {
        return date.toString().replace("-", "");
    }

    private static List<String> getNewsIds(CrawledNewsDto response) {
        YibKrA yibKrA = response.getYIB_KR_A();
        return yibKrA.getResult().stream()
            .map(result -> result.getCID())
            .collect(Collectors.toList());
    }

    private SummarizedNewsDto getSummarizedNews(String keyword, String id) {
        SummarizedNewsDto news = getNewsInfo(id);
        String summarizeNewsContent = summarizeNews(news.getContent(), keyword);
        news.setContent(summarizeNewsContent);
        return news;
    }

    private SummarizedNewsDto getNewsInfo(String id) {
        try {
            String baseUrl = "https://www.yna.co.kr/view/";
            String url = baseUrl + id;

            Document doc = Jsoup.connect(url).get();
            Elements contentElements = doc.select(".story-news p");

            String content = contentElements.stream()
                .map(Element::text)
                .map(String::trim)
                .filter(text -> !text.isEmpty())
                .collect(Collectors.joining(" "));

            Element dateElement = doc.selectFirst(".txt-time01");
            String date = dateElement.text().split(" ")[0].replaceAll("[^\\d-]", "").trim();

            return SummarizedNewsDto.of(url, content, date);
        } catch (IOException e) {
            throw new NewsCrawlingException("뉴스 크롤링 중 오류가 발생하였습니다: " + e.getMessage());
        }
    }

    public String summarizeNews(String content, String keyword) {
        try {
            String promptText = keyword.equals("keyword")
                ? SUMMARIZE_NEWS_BY_KEYWORD_PROMPT : SUMMARIZE_GENERAL_NEWS_PROMPT;

            List<String> vars = List.of(content, keyword);

            UserMessage prompt = createPrompt(promptText, vars);

            String response = model.chat(prompt).aiMessage().text();

            StringSummaryDto result = objectMapper.readValue(response, StringSummaryDto.class);

            return result.getSummary();
        } catch (JsonProcessingException e) {
            log.info("Json 파싱 중 예외가 발생했습니다.={}", e.getMessage());
            return "";
        }
    }

    private String createVar(List<SummarizedNewsDto> newsList) throws JsonProcessingException {
        StringBuilder vars = new StringBuilder();
        vars.append("[");
        for (SummarizedNewsDto news : newsList) {
            vars.append(objectMapper.writeValueAsString(news) + ",");
        }
        vars.deleteCharAt(vars.length() - 1);
        vars.append("]");
        return vars.toString();
    }

    private UserMessage createPrompt(String promptText, String var) {
        String prompt = promptText.formatted(var);
        UserMessage userMessage = new UserMessage(prompt);
        return userMessage;
    }

    private UserMessage createPrompt(String promptText, List<String> var) {
        String prompt = promptText.formatted(var.toArray());
        UserMessage userMessage = new UserMessage(prompt);
        return userMessage;
    }

    private News updateNews(String keyword, SummarizedMultipleNewsDto result) {
        News findNews = newsRepository.findByKeyword(keyword)
            .orElse(News.of(keyword));

        findNews.setContent(result.getSummary());

        List<NewsInfo> newsInfos = result.getSources().stream()
            .map(info -> NewsInfo.of(info.getUrl(), info.getDate()))
            .collect(Collectors.toList());
        findNews.setNewsInfos(newsInfos);

        return findNews;
    }
}