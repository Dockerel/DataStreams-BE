package datastreams_knu.bigpicture.report.service.dto;

import datastreams_knu.bigpicture.stock.entity.StockInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class StockPromptInputDto {
    private LocalDate stockDate;
    private double stockRate;

    @Builder
    public StockPromptInputDto(LocalDate stockDate, double stockRate) {
        this.stockDate = stockDate;
        this.stockRate = stockRate;
    }

    public static StockPromptInputDto from(StockInfo stockInfo) {
        return StockPromptInputDto.builder()
            .stockDate(stockInfo.getStockDate())
            .stockRate(stockInfo.getStockPrice())
            .build();
    }
}
