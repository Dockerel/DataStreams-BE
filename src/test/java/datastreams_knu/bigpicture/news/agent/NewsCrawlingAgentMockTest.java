package datastreams_knu.bigpicture.news.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import datastreams_knu.bigpicture.common.config.AiModelConfig;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import datastreams_knu.bigpicture.news.agent.dto.CrawledNewsDto;
import datastreams_knu.bigpicture.news.agent.dto.SummarizedNewsDto;
import datastreams_knu.bigpicture.news.repository.NewsRepository;
import datastreams_knu.bigpicture.news.repository.ReferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsCrawlingAgentMockTest {

    @Mock
    WebClientUtil webClientUtil;
    @Mock
    AiModelConfig aiModelConfig;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    NewsRepository newsRepository;
    @Mock
    ReferenceRepository referenceRepository;

    NewsCrawlingAgent newsCrawlingAgent;
    NewsCrawlingAgent spyNewsCrawlingAgent;

    @BeforeEach
    void setUp() {
        newsCrawlingAgent = new NewsCrawlingAgent(
            webClientUtil,
            aiModelConfig,
            objectMapper,
            newsRepository,
            referenceRepository
        );
        spyNewsCrawlingAgent = Mockito.spy(newsCrawlingAgent);

        List<String> ids = List.of("id1", "id2", "id3");
        CrawledNewsDto crawledNewsDto = CrawledNewsDto.of(ids);

        when(webClientUtil.get(any(), any()))
            .thenReturn(crawledNewsDto);

        doReturn(SummarizedNewsDto.of("url", "content", "date"))
            .when(spyNewsCrawlingAgent).getSummarizedNews(anyString(), anyString());
    }

    @DisplayName("keyword를 기반으로 관련 뉴스 기사를 수집합니다.")
    @Test
    void crawlingNewsByKeywordTest() {
        // given // when
        List<SummarizedNewsDto> result = spyNewsCrawlingAgent.crawlingNewsByKeyword("keyword");

        // then
        assertThat(result).hasSize(3);
    }

    @DisplayName("국내/해외 뉴스 기사를 수집합니다.")
    @Test
    void crawlingNewsGeneralTest() {
        // given // when
        List<SummarizedNewsDto> result = spyNewsCrawlingAgent.crawlingNewsGeneral("국내");

        // then
        assertThat(result).hasSize(3);
    }

}