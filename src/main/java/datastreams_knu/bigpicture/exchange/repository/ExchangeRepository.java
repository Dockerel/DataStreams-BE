package datastreams_knu.bigpicture.exchange.repository;

import datastreams_knu.bigpicture.exchange.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
}
