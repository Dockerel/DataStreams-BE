package datastreams_knu.bigpicture.alert.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SummarizedNewsDto {
    private String summary;

    @Builder
    public SummarizedNewsDto(String summary) {
        this.summary = summary;
    }

    public static SummarizedNewsDto of(String summary) {
        return SummarizedNewsDto.builder()
                .summary(summary)
                .build();
    }
}
