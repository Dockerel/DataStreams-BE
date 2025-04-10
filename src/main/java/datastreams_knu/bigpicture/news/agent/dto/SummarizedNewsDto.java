package datastreams_knu.bigpicture.news.agent.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class SummarizedNewsDto {

    private String url;
    private String content;
    private String date;

    @Builder
    public SummarizedNewsDto(String url, String content, String date) {
        this.url = url;
        this.content = content;
        this.date = date;
    }

    public static SummarizedNewsDto of(String url, String content, String date) {
        return SummarizedNewsDto.builder()
            .url(url)
            .content(content)
            .date(date)
            .build();
    }

    public void setContent(String summarizeNewsContent) {
        this.content = summarizeNewsContent;
    }
}
