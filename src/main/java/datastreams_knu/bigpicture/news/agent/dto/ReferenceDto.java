package datastreams_knu.bigpicture.news.agent.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReferenceDto {
    private String url;

    @Builder
    public ReferenceDto(String url) {
        this.url = url;
    }

    public static ReferenceDto of(String url) {
        return ReferenceDto.builder()
            .url(url)
            .build();
    }
}