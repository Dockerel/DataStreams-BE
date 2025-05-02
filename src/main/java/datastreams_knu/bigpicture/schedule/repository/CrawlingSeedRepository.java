package datastreams_knu.bigpicture.schedule.repository;

import datastreams_knu.bigpicture.schedule.entity.CrawlingSeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrawlingSeedRepository extends JpaRepository<CrawlingSeed, Long> {

    List<CrawlingSeed> findAllByStockType(String stockType);
}
