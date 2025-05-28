package datastreams_knu.bigpicture.news.agent.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SummarizedMultipleNewsDto {
    private String summary;
    private List<ReferenceDto> sources;

    @Builder
    public SummarizedMultipleNewsDto(String summary, List<ReferenceDto> sources) {
        this.summary = summary;
        this.sources = sources;
    }

    public static SummarizedMultipleNewsDto of(String summary, List<ReferenceDto> sources) {
        return SummarizedMultipleNewsDto.builder()
                .summary(summary)
                .sources(sources)
                .build();
    }
}