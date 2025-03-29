package datastreams_knu.bigpicture.ecos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class KeyStatisticDto {

    @JsonProperty("KEYSTAT_CODE")
    private String code;

    @JsonProperty("KEYSTAT_NAME")
    private String name;

    @JsonProperty("DATA_VALUE")
    private String value;

    @JsonProperty("CYCLE")
    private String cycle;

    @JsonProperty("UNIT_NAME")
    private String unit;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("TIME")
    private LocalDate date;
}
