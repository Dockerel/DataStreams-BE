package datastreams_knu.bigpicture.news.agent.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SummarizedMultipleNewsDto {
    private String summary;
    private List<NewsInfoDto> sources;
}