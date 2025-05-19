package datastreams_knu.bigpicture.alert.repository;

import datastreams_knu.bigpicture.alert.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    Optional<Watchlist> findByStockName(String stockName);
}
