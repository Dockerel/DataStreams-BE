package datastreams_knu.bigpicture.stock.repository;

import datastreams_knu.bigpicture.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByStockName(String stockName);
}
