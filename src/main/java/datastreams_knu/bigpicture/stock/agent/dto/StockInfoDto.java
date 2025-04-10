package datastreams_knu.bigpicture.stock.agent.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StockInfoDto {
    private LocalDate stockDate;
    private double stockPrice;

    @Builder
    public StockInfoDto(LocalDate stockDate, double stockPrice) {
        this.stockDate = stockDate;
        this.stockPrice = stockPrice;
    }

    public static StockInfoDto of(LocalDate stockDate, double stockPrice) {
        return StockInfoDto.builder()
            .stockDate(stockDate)
            .stockPrice(stockPrice)
            .build();
    }
}
