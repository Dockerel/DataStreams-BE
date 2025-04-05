package datastreams_knu.bigpicture.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CrawlingResultDto {

    private Boolean result;
    private String message;

    @Builder
    public CrawlingResultDto(Boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public static CrawlingResultDto of(Boolean result, String message) {
        return CrawlingResultDto.builder()
            .result(result)
            .message(message)
            .build();
    }
}
