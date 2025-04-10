package datastreams_knu.bigpicture.news.agent.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReferenceDto {
    private String url;
    private String date;

    @Builder
    public ReferenceDto(String url, String date) {
        this.url = url;
        this.date = date;
    }

    public static ReferenceDto of(String url, String date) {
        return ReferenceDto.builder()
            .url(url)
            .date(date)
            .build();
    }
}