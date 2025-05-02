package datastreams_knu.bigpicture.exchange.repository;

import datastreams_knu.bigpicture.exchange.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    int deleteAllByExchangeDateBefore(LocalDate localDate);
}
