package datastreams_knu.bigpicture.news.repository;

import datastreams_knu.bigpicture.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    Optional<News> findByKeyword(String keyword);
}
