package datastreams_knu.bigpicture.news.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datastreams_knu.bigpicture.common.config.AiModelConfig;
import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import datastreams_knu.bigpicture.common.dto.DateRangeDto;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import datastreams_knu.bigpicture.news.agent.NewsCrawlingAssistant;
import datastreams_knu.bigpicture.news.agent.dto.CrawledNewsDto;
import datastreams_knu.bigpicture.news.agent.dto.StringSummaryDto;
import datastreams_knu.bigpicture.news.agent.dto.SummarizedMultipleNewsDto;
import datastreams_knu.bigpicture.news.agent.dto.SummarizedNewsDto;
import datastreams_knu.bigpicture.news.config.NewsCrawlingConfig;
import datastreams_knu.bigpicture.news.entity.News;
import datastreams_knu.bigpicture.news.entity.Reference;
import datastreams_knu.bigpicture.news.exception.NewsCrawlingException;
import datastreams_knu.bigpicture.news.repository.NewsRepository;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static datastreams_knu.bigpicture.news.agent.AgentPrompt.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NewsCrawlingService {

    private final NewsCrawlingConfig newsCrawlingConfig;
    private final WebClientUtil webClientUtil;
    private final AiModelConfig aiModelConfig;
    private final ObjectMapper objectMapper;
    private final NewsRepository newsRepository;

    private NewsCrawlingAssistant newsCrawlingAssistant;
    private ChatLanguageModel model;

    @PostConstruct
    public void init() {
        this.newsCrawlingAssistant = newsCrawlingConfig.newsCrawlingAssistant();
        this.model = aiModelConfig.openAiChatModel();
    }

    @Transactional
    public CrawlingResultDto crawling(String type, String keyword) {
        return newsCrawlingAssistant.execute(type, keyword);
    }

    @Transactional
    public CrawlingResultDto dataInit(String keyword) {
        Map<String, String> params = Map.of("&query=", URLEncoder.encode(keyword, StandardCharsets.UTF_8));

        String url = createUrl(params);

        CrawledNewsDto newsInfoResponse = webClientUtil.get(url, CrawledNewsDto.class);

        List<String> newsIds = getNewsIds(newsInfoResponse);

        List<SummarizedNewsDto> summarizedNewsList = newsIds.stream()
                .map(id -> getSummarizedNews(keyword, id))
                .collect(Collectors.toList());

        String promptText = SUMMARIZE_MULTIPLE_NEWS_PROMPT;

        String var = createVar(summarizedNewsList);

        UserMessage prompt = createPrompt(promptText, var);

        String summarizedNewsResponse = model.chat(prompt).aiMessage().text();

        SummarizedMultipleNewsDto summarizedMultipleNews;
        try {
            summarizedMultipleNews = objectMapper.readValue(summarizedNewsResponse, SummarizedMultipleNewsDto.class);
        } catch (JsonProcessingException e) {
            throw new NewsCrawlingException("뉴스 크롤링 중 오류가 발생하였습니다: " + e.getMessage());
        }

        News news = saveNews(keyword, summarizedMultipleNews);
        newsRepository.save(news);

        return CrawlingResultDto.of(true, "뉴스 크롤링 성공");
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

        DateRangeDto range = getDateRange();
        stringBuilder
                .append("&from=")
                .append(range.getFromDate())
                .append("&to=")
                .append(range.getToDate());

        return stringBuilder.toString();
    }

    private static DateRangeDto getDateRange() {
        LocalDate lastWeek = LocalDate.now().minusDays(7);
        return DateRangeDto.of(processDate(lastWeek), processDate(LocalDate.now()));
    }

    private static String processDate(LocalDate date) {
        return date.toString().replace("-", "");
    }

    private static List<String> getNewsIds(CrawledNewsDto response) {
        CrawledNewsDto.YibKrA yibKrA = response.getYIB_KR_A();
        return yibKrA.getResult().stream()
                .map(result -> result.getCID())
                .collect(Collectors.toList());
    }

    protected SummarizedNewsDto getSummarizedNews(String keyword, String id) {
        SummarizedNewsDto news = getReference(id);
        String summarizeNewsContent = summarizeNews(news.getContent(), keyword);
        news.setContent(summarizeNewsContent);
        return news;
    }

    protected SummarizedNewsDto getReference(String id) {
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

    private String createVar(List<SummarizedNewsDto> newsList) {
        try {
            StringBuilder vars = new StringBuilder();
            vars.append("[");
            for (SummarizedNewsDto news : newsList) {
                vars.append(objectMapper.writeValueAsString(news) + ",");
            }
            vars.deleteCharAt(vars.length() - 1);
            vars.append("]");
            return vars.toString();
        } catch (JsonProcessingException e) {
            log.info("Json 파싱 중 예외가 발생했습니다.={}", e.getMessage());
            return "";
        }
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

    private News saveNews(String keyword, SummarizedMultipleNewsDto result) {
        News news = News.of(LocalDate.now(), keyword, result.getSummary());

        result.getSources().stream()
                .map(info -> Reference.of(info.getUrl()))
                .forEach(news::addReference);

        return news;
    }
}
