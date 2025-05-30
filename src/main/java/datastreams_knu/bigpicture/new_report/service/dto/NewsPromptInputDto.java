package datastreams_knu.bigpicture.new_report.service.dto;

import datastreams_knu.bigpicture.news.entity.News;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class NewsPromptInputDto {
    private LocalDate newsDate;
    private String content;

    @Builder
    public NewsPromptInputDto(LocalDate newsDate, String content) {
        this.newsDate = newsDate;
        this.content = content;
    }

    public static NewsPromptInputDto from(News news) {
        return NewsPromptInputDto.builder()
                .newsDate(news.getNewsCrawlingDate())
                .content(news.getContent())
                .build();
    }
}
