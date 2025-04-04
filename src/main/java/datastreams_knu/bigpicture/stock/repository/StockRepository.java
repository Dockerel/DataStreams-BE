package datastreams_knu.bigpicture.stock.repository;

import datastreams_knu.bigpicture.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByName(String name);
}
