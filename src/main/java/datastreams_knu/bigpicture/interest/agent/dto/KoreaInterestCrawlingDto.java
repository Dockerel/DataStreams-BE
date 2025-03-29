package datastreams_knu.bigpicture.interest.agent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
public class KoreaInterestCrawlingDto {
    @JsonProperty("StatisticSearch")
    private StatisticSearch statisticSearch;

    @Getter
    public static class StatisticSearch {
        @JsonProperty("row")
        private List<StatisticRow> row;
    }

    @ToString
    @Getter
    public static class StatisticRow {
        @JsonProperty("TIME")
        private String time;

        @JsonProperty("DATA_VALUE")
        private String dataValue;
    }
}