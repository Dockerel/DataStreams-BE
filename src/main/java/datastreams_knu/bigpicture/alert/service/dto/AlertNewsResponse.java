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
    public String title;

    @Builder
    public AlertNewsResponse(LocalDateTime newsLocalDateTime, String url, String keyword, String title) {
        this.newsLocalDateTime = newsLocalDateTime;
        this.url = url;
        this.keyword = keyword;
        this.title = title;
    }

    public static AlertNewsResponse of(LocalDateTime newsLocalDateTime, String url, String keyword, String title) {
        return AlertNewsResponse.builder()
                .newsLocalDateTime(newsLocalDateTime)
                .url(url)
                .keyword(keyword)
                .title(title)
                .build();
    }

    public static AlertNewsResponse from(Result result) {
        String dateStr = result.getDATETIME();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);

        String url = "https://www.yna.co.kr/view/" + result.getCID();

        return AlertNewsResponse.of(
                dateTime,
                url,
                result.getKEYWORD().replace("\r\n", ", "),
                result.getEDIT_TITLE()
        );
    }
}
