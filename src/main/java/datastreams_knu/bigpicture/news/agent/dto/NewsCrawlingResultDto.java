package datastreams_knu.bigpicture.news.agent.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewsCrawlingResultDto {
    private Boolean result;
    private String message;

    @Builder
    public NewsCrawlingResultDto(Boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public static NewsCrawlingResultDto of(Boolean result, String message) {
        return NewsCrawlingResultDto.builder()
            .result(result)
            .message(message)
            .build();
    }
}
