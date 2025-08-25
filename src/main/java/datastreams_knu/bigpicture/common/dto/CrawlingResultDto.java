package datastreams_knu.bigpicture.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class CrawlingResultDto {

    private Boolean result;
    private String message;

    public static CrawlingResultDto of(Boolean result, String message) {
        return CrawlingResultDto.builder()
                .result(result)
                .message(message)
                .build();
    }
}
