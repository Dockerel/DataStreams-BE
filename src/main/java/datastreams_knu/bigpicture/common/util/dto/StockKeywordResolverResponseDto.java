package datastreams_knu.bigpicture.common.util.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockKeywordResolverResponseDto {
    private String status;
    @JsonProperty("data")
    private String stockName;
}
