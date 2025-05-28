package datastreams_knu.bigpicture.news.repository;

import datastreams_knu.bigpicture.news.entity.News;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class NewsRepositoryTest {

    @Autowired
    NewsRepository newsRepository;
    @Autowired
    ReferenceRepository referenceRepository;

    @DisplayName("기준 날짜 이전에 수집된 뉴스 데이터들을 모두 삭제한다.")
    @Test
    void deleteAllByInterestDateBeforeTest() {
        // given
        LocalDate now = LocalDate.now();

        List<News> newss = List.of(
                News.of(now, "keyword3", "content3"),
                News.of(now.minusDays(8), "keyword2", "content2"),
                News.of(now.minusDays(8), "keyword2", "content2")
        );
        newsRepository.saveAll(newss);

        // when
        int result = newsRepository.deleteAllByNewsCrawlingDateBefore(now.minusDays(7));

        // then
        assertThat(result).isEqualTo(2);
        assertThat(newsRepository.findAll()).hasSize(1);
    }
}