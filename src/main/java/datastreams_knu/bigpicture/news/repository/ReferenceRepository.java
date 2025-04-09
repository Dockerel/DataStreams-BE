package datastreams_knu.bigpicture.news.repository;

import datastreams_knu.bigpicture.news.entity.NewsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsInfoRepository extends JpaRepository<NewsInfo, Long> {
    void deleteAllByNewsId(Long newsId);
}
