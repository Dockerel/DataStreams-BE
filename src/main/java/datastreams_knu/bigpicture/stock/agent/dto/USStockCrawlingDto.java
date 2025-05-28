package datastreams_knu.bigpicture.stock.agent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class USStockCrawlingDto {
    private String status;
    private List<Data> data;

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class Data {
        @JsonProperty("Date")
        private String date;
        @JsonProperty("Close")
        private double closePrice;

        public static Data of(String date, double closePrice) {
            return new Data(date, closePrice);
        }
    }

    public static USStockCrawlingDto of(List<Data> data) {
        return USStockCrawlingDto.builder()
                .data(data)
                .build();
    }
}