package datastreams_knu.bigpicture.news.domain;

import datastreams_knu.bigpicture.news.repository.NewsInfoRepository;
import datastreams_knu.bigpicture.news.repository.NewsRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NewsTest {

    @Autowired
    NewsRepository newsRepository;
    @Autowired
    NewsInfoRepository newsInfoRepository;
    @Autowired
    EntityManager em;

    @DisplayName("뉴스에 뉴스 정보를 지정하고 뉴스를 저장하면 뉴스 정보도 저장되어야 한다.")
    @Test
    void NewsPersistenceCascadeTest() {
        // given
        News news = News.of("keyword", "content");

        NewsInfo newsInfo = NewsInfo.of("url", "date");
        news.setNewsInfos(List.of(newsInfo));

        // when
        News saveNews = newsRepository.save(news);
        em.flush();
        em.clear();

        List<NewsInfo> findNewsInfos = newsInfoRepository.findAllByNewsId(saveNews.getId());

        // then
        assertThat(findNewsInfos.size()).isEqualTo(1);
    }

}