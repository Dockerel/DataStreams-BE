package datastreams_knu.bigpicture.exchange.agent.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
public class ExchangeCrawlingDto {
    public List<Data> data;

    @Getter
    @ToString
    public static class Data {
        public String date;
        public String rate;
    }
}
