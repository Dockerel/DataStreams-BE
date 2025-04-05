package datastreams_knu.bigpicture.stock.agent.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StockCrawlingResultDto {
    private Boolean result;
    private String message;

    @Builder
    public StockCrawlingResultDto(Boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public static StockCrawlingResultDto of(Boolean result, String message) {
        return StockCrawlingResultDto.builder()
            .result(result)
            .message(message)
            .build();
    }
}
