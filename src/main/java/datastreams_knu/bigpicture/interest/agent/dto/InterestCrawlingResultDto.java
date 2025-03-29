package datastreams_knu.bigpicture.interest.agent.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InterestCrawlingResultDto {

    private Boolean result;
    private String message;

    @Builder
    public InterestCrawlingResultDto(Boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public static InterestCrawlingResultDto of(Boolean result, String message) {
        return InterestCrawlingResultDto.builder()
            .result(result)
            .message(message)
            .build();
    }
}
