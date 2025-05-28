package datastreams_knu.bigpicture.stock.controller.dto;

import datastreams_knu.bigpicture.stock.entity.StockInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class StockResponse {
    private LocalDate stockDate;
    private double stockPrice;

    @Builder
    public StockResponse(LocalDate stockDate, double stockPrice) {
        this.stockDate = stockDate;
        this.stockPrice = stockPrice;
    }

    public static StockResponse of(LocalDate stockDate, double stockPrice) {
        return StockResponse.builder()
                .stockDate(stockDate)
                .stockPrice(stockPrice)
                .build();
    }

    public static StockResponse from(StockInfo stockInfo) {
        return StockResponse.of(stockInfo.getStockDate(), stockInfo.getStockPrice());
    }
}
