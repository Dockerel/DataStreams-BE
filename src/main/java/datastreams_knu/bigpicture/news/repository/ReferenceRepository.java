package datastreams_knu.bigpicture.news.repository;

import datastreams_knu.bigpicture.news.entity.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Long> {
    void deleteAllByNewsId(Long newsId);
}
