package datastreams_knu.bigpicture.schedule.service.dto;

import datastreams_knu.bigpicture.stock.entity.StockType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StockDto {

    private String name;
    private StockType stockType;

    @Builder
    public StockDto(String name, StockType stockType) {
        this.name = name;
        this.stockType = stockType;
    }

    public static StockDto of(String name, StockType stockType) {
        return StockDto.builder()
            .name(name)
            .stockType(stockType)
            .build();
    }
}
