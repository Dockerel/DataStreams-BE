package datastreams_knu.bigpicture.news.repository;

import datastreams_knu.bigpicture.news.domain.NewsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsInfoRepository extends JpaRepository<NewsInfo, Long> {
    List<NewsInfo> findAllByNewsId(Long newsId);
}
