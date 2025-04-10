package datastreams_knu.bigpicture.interest.agent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KoreaInterestCrawlingDto {
    @JsonProperty("StatisticSearch")
    private StatisticSearch statisticSearch;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StatisticSearch {
        @JsonProperty("row")
        private List<StatisticRow> row;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatisticRow {
        @JsonProperty("TIME")
        private String time;

        @JsonProperty("DATA_VALUE")
        private String dataValue;
    }

    public static KoreaInterestCrawlingDto of(List<StatisticRow> rows) {
        StatisticSearch statisticSearch = StatisticSearch.builder()
            .row(rows)
            .build();
        return KoreaInterestCrawlingDto.builder()
            .statisticSearch(statisticSearch)
            .build();
    }
}