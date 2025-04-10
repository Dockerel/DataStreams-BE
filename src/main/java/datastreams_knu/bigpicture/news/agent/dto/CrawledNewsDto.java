package datastreams_knu.bigpicture.news.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrawledNewsDto {
    public YibKrA YIB_KR_A;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class YibKrA {
        public List<Result> result;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        public String CID;

        public static Result of(String cid) {
            return new Result(cid);
        }
    }

    public static CrawledNewsDto of(List<String> ids) {
        List<Result> results = ids.stream()
            .map(id -> Result.of(id))
            .collect(Collectors.toList());
        YibKrA yibKrA = new YibKrA(results);
        return CrawledNewsDto.builder()
            .YIB_KR_A(yibKrA)
            .build();
    }
}
