package datastreams_knu.bigpicture.stock.repository;

import datastreams_knu.bigpicture.stock.entity.Stock;
import datastreams_knu.bigpicture.stock.entity.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockInfoRepository extends JpaRepository<StockInfo, Long> {

    @Modifying
    @Query("delete from StockInfo si where si.stock.id = :stockId")
    void deleteAllByStockId(@Param("stockId") Long stockId);
}
