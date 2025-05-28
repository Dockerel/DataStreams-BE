package datastreams_knu.bigpicture.stock.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckTickerResponseDto {
    private String status;
    @JsonProperty("data")
    private Boolean isValidTicker;
}
