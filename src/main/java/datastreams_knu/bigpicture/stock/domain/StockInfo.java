package datastreams_knu.bigpicture.stock.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StockInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate stockDate;

    private double stockPrice;

    @Builder
    public StockInfo(double stockPrice, LocalDate stockDate) {
        this.stockPrice = stockPrice;
        this.stockDate = stockDate;
    }

    public static StockInfo of(double stockPrice, LocalDate stockDate) {
        return StockInfo.builder()
            .stockPrice(stockPrice)
            .stockDate(stockDate)
            .build();
    }
}
