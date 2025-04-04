package datastreams_knu.bigpicture.stock.repository;

import datastreams_knu.bigpicture.stock.domain.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockInfoRepository extends JpaRepository<StockInfo, Long> {
}
