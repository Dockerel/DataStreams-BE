package datastreams_knu.bigpicture.alert.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static datastreams_knu.bigpicture.alert.service.dto.CrawledNewsDto.Result;

@ToString
@NoArgsConstructor
@Getter
public class AlertNewsResponse {
    public LocalDateTime newsLocalDateTime;
    public String url;
    public String keyword;
    public String content;
    public String title;
    public String writer;
    public String thumbnailUrl;

    @Builder
    public AlertNewsResponse(LocalDateTime newsLocalDateTime, String url, String keyword, String content, String title, String writer, String thumbnailUrl) {
        this.newsLocalDateTime = newsLocalDateTime;
        this.url = url;
        this.keyword = keyword;
        this.content = content;
        this.title = title;
        this.writer = writer;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static AlertNewsResponse from(Result result, String summarizedContent) {
        String dateStr = result.getDATETIME();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);

        String url = "https://www.yna.co.kr/view/" + result.getCID();

        String thumbnailBaseUrl = "https://img4.yna.co.kr";
        String thumbnailUrl = thumbnailBaseUrl + result.getTHUMBNAIL().replace("T.jpg", "P4.jpg");

        return AlertNewsResponse.builder()
                .newsLocalDateTime(dateTime)
                .url(url)
                .keyword(result.getKEYWORD().replace("\r\n", ", "))
                .content(summarizedContent)
                .title(result.getEDIT_TITLE())
                .writer(result.getWRITER_NAME())
                .thumbnailUrl(thumbnailUrl)
                .build();
    }
}
