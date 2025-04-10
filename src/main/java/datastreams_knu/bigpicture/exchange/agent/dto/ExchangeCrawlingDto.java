package datastreams_knu.bigpicture.exchange.agent.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ExchangeCrawlingDto {
    public List<Data> data;

    @Getter
    @NoArgsConstructor
    public static class Data {
        public String date;
        public String rate;

        public Data(String date, String rate) {
            this.date = date;
            this.rate = rate;
        }
    }

    @Builder
    public ExchangeCrawlingDto(List<Data> data) {
        this.data = data;
    }

    public static ExchangeCrawlingDto of(List<Data> data) {
        return ExchangeCrawlingDto.builder()
            .data(data)
            .build();
    }
}
