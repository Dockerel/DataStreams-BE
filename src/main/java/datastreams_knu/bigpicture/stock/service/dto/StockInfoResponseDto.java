package datastreams_knu.bigpicture.stock.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StockInfoResponseDto {
    private String status;
    private List<Data> data;

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @ToString
    public static class Data {
        @JsonProperty("Date")
        private String date;
        @JsonProperty("Close")
        private double closePrice;
    }
}