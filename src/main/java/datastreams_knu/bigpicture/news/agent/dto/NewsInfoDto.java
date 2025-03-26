package datastreams_knu.bigpicture.news.agent.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsInfoDto {
    private String url;
    private String date;

    @Builder
    public NewsInfoDto(String url, String date) {
        this.url = url;
        this.date = date;
    }

    public static NewsInfoDto of(String url, String date) {
        return NewsInfoDto.builder()
            .url(url)
            .date(date)
            .build();
    }
}