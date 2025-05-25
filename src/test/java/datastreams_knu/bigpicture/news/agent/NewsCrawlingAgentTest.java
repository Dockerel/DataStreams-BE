package datastreams_knu.bigpicture.news.agent;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import datastreams_knu.bigpicture.news.agent.dto.ReferenceDto;
import datastreams_knu.bigpicture.news.agent.dto.SummarizedMultipleNewsDto;
import datastreams_knu.bigpicture.news.entity.News;
import datastreams_knu.bigpicture.news.repository.NewsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class NewsCrawlingAgentTest {

    @Autowired
    NewsCrawlingAgent newsCrawlingAgent;
    @Autowired
    NewsRepository newsRepository;

    @DisplayName("keyword, 생성된 기사 요약과 기사 출처 URL들 그리고 해당 기사의 날짜들을 DB에 저장합니다.")
    @Test
    void saveSummarizedNewsWithUrlsTest() {
        // given
        String keyword = "keyword";

        List<ReferenceDto> referenceDtos = List.of(
            ReferenceDto.of("url1"),
            ReferenceDto.of("url2"),
            ReferenceDto.of("url3")
        );

        SummarizedMultipleNewsDto summarizedMultipleNewsDto = SummarizedMultipleNewsDto.of("summary", referenceDtos);

        // when
        CrawlingResultDto result = newsCrawlingAgent.saveSummarizedNewsWithUrls(keyword, summarizedMultipleNewsDto);

        Optional<News> findNewsOptional = newsRepository.findByKeyword(keyword);

        // then
        assertThat(result.getResult()).isTrue();
        assertThat(result.getMessage()).isEqualTo("뉴스 크롤링 성공");

        assertThat(findNewsOptional).isPresent();

        News findNews = findNewsOptional.get();
        assertThat(findNews.getReferences()).hasSize(3);
    }
}
