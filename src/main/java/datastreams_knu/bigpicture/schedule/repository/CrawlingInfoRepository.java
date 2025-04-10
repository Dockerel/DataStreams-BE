package datastreams_knu.bigpicture.schedule.repository;

import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrawlingInfoRepository extends JpaRepository<CrawlingInfo, Long> {

    Optional<CrawlingInfo> findByStockName(String stockName);
}
