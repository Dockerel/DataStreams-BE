package datastreams_knu.bigpicture.interest.agent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
public class USInterestCrawlingDto {
    @JsonProperty("observations")
    private List<ObservationsRow> observations;

    @ToString
    @Getter
    public static class ObservationsRow {
        @JsonProperty("date")
        private String time;
        @JsonProperty("value")
        private String dataValue;
    }
}
