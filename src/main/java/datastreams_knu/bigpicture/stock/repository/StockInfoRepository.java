package datastreams_knu.bigpicture.stock.repository;

import datastreams_knu.bigpicture.stock.entity.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface StockInfoRepository extends JpaRepository<StockInfo, Long> {

    int deleteAllByStockDateBefore(LocalDate localDate);
}
